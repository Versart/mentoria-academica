package com.versart.mentoria_academica.security;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.versart.mentoria_academica.api.model.DadosSUAPResponse;
import com.versart.mentoria_academica.api.model.TokenRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityFilter extends OncePerRequestFilter{

    private final String urlVerificarToken = "https://suap.ifma.edu.br/api/token/verify";

    private final String urlDados = "https://suap.ifma.edu.br/api/rh/meus-dados";

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            TokenRequest token = new TokenRequest(pegarToken(request));
            log.info("Entrando no filtro");
            if(token.token() != null) {
                try{
                    log.info("Verifiquei que tenho token");
                    HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(token);
                    ResponseEntity<String> respostaToken = restTemplate.exchange(urlVerificarToken, HttpMethod.POST,
                    httpEntity,String.class );
                    List<GrantedAuthority> permissoes;
                    if(respostaToken.getStatusCode().is2xxSuccessful()) {
                        log.info("Token verificado");
                        String userId = PegarUserIdToken(token.token());
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setBearerAuth(token.token());
                        HttpEntity entity = new HttpEntity<>(httpHeaders);
                        ResponseEntity<DadosSUAPResponse> respostaDados = restTemplate.exchange(urlDados, HttpMethod.GET, entity, DadosSUAPResponse.class);
                        if(!respostaDados.getBody().getTipoVinculo().contains("Aluno")){
                            permissoes = List.of("ROLE_ADMIN").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                        }
                        else
                            permissoes = List.of();
                        log.info("Já inseri as permissões");
                        var authentication = new UsernamePasswordAuthenticationToken(userId, null,permissoes);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("sucesso");
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                }
                catch(HttpClientErrorException | HttpServerErrorException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            filterChain.doFilter(request, response);
        
    }

    private String PegarUserIdToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length >= 2) {
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(payload);
                JsonNode userIdNode = jsonNode.get("user_id");
                if(userIdNode!=null) {
                    return "user-" + userIdNode.asText();
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private String pegarToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if(token != null) 
            return token.replace("Bearer ", "");
        return null;
    }
    
}

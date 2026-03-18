package com.versart.mentoria_academica.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.versart.mentoria_academica.api.model.TokenRequest;
import com.versart.mentoria_academica.domain.service.TokenService;

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

    
    private final TokenService tokenService;

    @Value("${url.document}")
    private String serverUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            log.info("Entrando no filtro");
            String token = pegarToken(request);

            TokenRequest tokenRequest = new TokenRequest(token);
            
            log.info("Verificando o token {}", token);
            if(token != null && tokenService.isTokenValido(token)) {
                try{                    
                    List<GrantedAuthority> permissoes;
                    String userId = tokenService.getSubject(token);
                    String role = tokenService.getRole(token);
                    if(!role.contains("Aluno")){
                        permissoes = List.of("ROLE_ADMIN").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    }
                    else{
                        permissoes = List.of("ROLE_USER").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    }
                    var authentication = new UsernamePasswordAuthenticationToken(userId, null,permissoes);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                catch(HttpClientErrorException | HttpServerErrorException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    log.info("Erro ao verificar token {}", e.getMessage());
                    return;
                }
            }
            filterChain.doFilter(request, response);
        
    }

    /*private String PegarUserIdToken(String token) {
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
    }*/

    private String pegarToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if(token != null) 
            return token.replace("Bearer ", "");
        return null;
    }
    
}

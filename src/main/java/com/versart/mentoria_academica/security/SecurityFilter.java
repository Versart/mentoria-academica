package com.versart.mentoria_academica.security;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.versart.mentoria_academica.api.model.TokenRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter{

    private final String urlVerificarToken = "https://suap.ifma.edu.br/api/token/verify";

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            TokenRequest token = new TokenRequest(pegarToken(request));

            if(token.token() != null) {
                try{
                    HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(token);
                    ResponseEntity<String> resposta = restTemplate.exchange(urlVerificarToken, HttpMethod.POST,
                    httpEntity,String.class );
                    if(resposta.getStatusCode().is2xxSuccessful()) {
                        String userId = PegarUserIdToken(token.token());
                        var authentication = new UsernamePasswordAuthenticationToken(userId, null,List.of());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
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

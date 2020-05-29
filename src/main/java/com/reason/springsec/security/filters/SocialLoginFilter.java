package com.reason.springsec.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reason.springsec.dtos.SocialLoginDto;
import com.reason.springsec.security.tokens.PreAuthorizationToken;
import com.reason.springsec.security.tokens.SocialPreAuthorizationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SocialLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationSuccessHandler successHandler;
    protected SocialLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public SocialLoginFilter(String url, AuthenticationSuccessHandler handler){
        super(url);
        this.successHandler = handler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        SocialLoginDto dto = objectMapper.readValue(request.getReader(), SocialLoginDto.class);
        return super.getAuthenticationManager().authenticate(new SocialPreAuthorizationToken(dto));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }
}

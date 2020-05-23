package com.reason.springsec.security.filters;

import com.reason.springsec.security.jwt.HeaderTokenExtractor;
import com.reason.springsec.security.jwt.InvalidJwtException;
import com.reason.springsec.security.handlers.JwtAuthenticationFailureHandler;
import com.reason.springsec.security.tokens.JwtPreProcessingToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private JwtAuthenticationFailureHandler failureHandler;
    private HeaderTokenExtractor extractor;

    protected JwtAuthenticationFilter(RequestMatcher matcher) {
        super(matcher);
    }

    public JwtAuthenticationFilter(RequestMatcher matcher, JwtAuthenticationFailureHandler handler, HeaderTokenExtractor extractor){
        super(matcher);
        this.failureHandler = handler;
        this.extractor = extractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String tokenPayload = request.getHeader("Authorization");
        JwtPreProcessingToken token;
        try {
            token = new JwtPreProcessingToken(this.extractor.extract(tokenPayload));
        } catch (InvalidJwtException e1){
            token = new JwtPreProcessingToken(null);
        }

        return super.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context); // 컨텍스트를 만들어서 컨텍스트를 보관시킴

        // chain을 이용해서 이후 동작을 수행해야함
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 현재 컨텍스트를 지운다 (비정상 토큰을 받은 상태이기 때문)
        SecurityContextHolder.clearContext();
    }
}

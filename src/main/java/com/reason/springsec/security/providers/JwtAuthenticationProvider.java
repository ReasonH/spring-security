package com.reason.springsec.security.providers;

import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.jwt.InvalidJwtException;
import com.reason.springsec.security.jwt.JwtDecoder;
import com.reason.springsec.security.tokens.JwtPreProcessingToken;
import com.reason.springsec.security.tokens.PostAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String)authentication.getPrincipal();
        AccountContext context;

        try {
            context = jwtDecoder.decodeJwt(token);
        } catch (InvalidJwtException e){
            context = null;
        }
        return PostAuthorizationToken.getTokenFromAccountContext(context);
    }

    @Override // support 토큰 설정
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}

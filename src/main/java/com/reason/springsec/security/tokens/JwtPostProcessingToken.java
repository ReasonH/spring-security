package com.reason.springsec.security.tokens;

import com.reason.springsec.domain.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtPostProcessingToken extends UsernamePasswordAuthenticationToken {

    private JwtPostProcessingToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public JwtPostProcessingToken(String username, UserRole role){
        super(username, "1234", parseAuthorities(role));
    }

    public static Collection<? extends GrantedAuthority> parseAuthorities(UserRole role){
        return Stream.of(role).map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    public String getUserId(){
        return (String)super.getPrincipal();
    }

    public String getPassword(){
        return (String)super.getCredentials();
    }
}

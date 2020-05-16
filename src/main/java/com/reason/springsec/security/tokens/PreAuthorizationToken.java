package com.reason.springsec.security.tokens;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// 인증전 객체
public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    public PreAuthorizationToken(String username, String password) {
        super(username, password);
    }

    // getPrincipal 등을 직접 쓰는 것보다 메소드 이름에서 유추가 가능하도록
    // 변경시킨다
    public String getUsername(){
        return (String)super.getPrincipal();
    }

    public String getUserPassword(){
        return (String)super.getCredentials();
    }
}
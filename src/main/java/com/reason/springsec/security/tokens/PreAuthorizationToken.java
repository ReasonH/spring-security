package com.reason.springsec.security.tokens;

import com.reason.springsec.domain.SocialProviders;
import com.reason.springsec.dtos.FormLoginDto;
import com.reason.springsec.dtos.SocialLoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// 인증전 객체
public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(String username, String password) {
        super(username, password);
    }

    private PreAuthorizationToken(SocialProviders providers, SocialLoginDto dto) {
        super(providers, dto);
    }

    public PreAuthorizationToken(FormLoginDto dto){
        this(dto.getId(), dto.getPassword());
    }

    public PreAuthorizationToken(SocialLoginDto dto) {
        this(dto.getProvider(), dto);
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

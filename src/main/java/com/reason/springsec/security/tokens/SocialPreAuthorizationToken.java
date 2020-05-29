package com.reason.springsec.security.tokens;

import com.reason.springsec.dtos.FormLoginDto;
import com.reason.springsec.dtos.SocialLoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SocialPreAuthorizationToken extends UsernamePasswordAuthenticationToken {
    public SocialPreAuthorizationToken(SocialLoginDto dto) {
        super(dto.getProvider(), dto);
    }

    public SocialLoginDto getDto(){
        return (SocialLoginDto)super.getCredentials();
    }
}

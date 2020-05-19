package com.reason.springsec.security.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reason.springsec.dtos.TokenDto;
import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.JwtFactory;
import com.reason.springsec.security.tokens.PostAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 프로바이더에서 넘어온 postauth 토큰을 받는다.
// 토큰 값을 DTO로 만들어서 response 로 내려주는 역할 수행
@Component
public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtFactory factory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // account context 추출
        PostAuthorizationToken token = (PostAuthorizationToken) authentication;

        // context 내의 유저 객체 정보
        AccountContext context = (AccountContext) token.getPrincipal();

        // 객체 정보를 토큰 claimset 값으로 넣어서 생성
        String tokenString = factory.generateToken(context);

        // 토큰스트링으로 DTO 생성하여 response 반환
        processResponse(response, writeDto(tokenString));
    }

    private TokenDto writeDto(String tokenString) {
        return new TokenDto(tokenString);
    }

    private void processResponse(HttpServletResponse response, TokenDto dto) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(dto)); // object mapper로 json 타입 반환
    }
}

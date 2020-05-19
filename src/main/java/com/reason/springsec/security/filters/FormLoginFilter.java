package com.reason.springsec.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reason.springsec.dtos.FormLoginDto;
import com.reason.springsec.security.tokens.PreAuthorizationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 로그인 요청 걸러내는 필터
// 필터는 컴포넌트로 구현하지 않는다 -> 프로세싱 url이 정해져 있지 않기 때문
// 필터를 bean 등록한다고 해서 의존성 주입 받을 수 있는 부분이 존재하지 않는다
public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;

    public FormLoginFilter(String defaultUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler){
        super(defaultUrl);
        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
    }

    protected FormLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    // 처음 인증 시도
    // provider의 authenticate 메소드 호출하여 인증 진행
    // 인증 결과로부터 성공시: 인증 객체, 실패시: 예외
    // 그에 따른 로직 작성하면 됨
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        FormLoginDto dto = new ObjectMapper().readValue(request.getReader(), FormLoginDto.class);

        PreAuthorizationToken token = new PreAuthorizationToken(dto);
        return super.getAuthenticationManager().authenticate(token);
    }

    // 인증 성공 시 jwt 토큰을 만들어서 내려줘야함
    // 따라서 이 메소드는 jwt 토큰을 만들고, http response로 내려준다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    // 유저한테 정보를 알려준다
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }
}

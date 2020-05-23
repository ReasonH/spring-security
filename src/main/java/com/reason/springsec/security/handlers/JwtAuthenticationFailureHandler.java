package com.reason.springsec.security.handlers;

import com.reason.springsec.security.filters.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    // TODO 의미있는 값 전달로 변경 (현재는 단순 로그만 출력, 실제로는 사용자들에게 의미있는 값을 전달해야함)
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 실패시 로그등록
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error(exception.getMessage());
    }
}

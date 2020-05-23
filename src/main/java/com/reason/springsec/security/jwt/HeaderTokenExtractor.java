package com.reason.springsec.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// 토큰값 추출
@Component
public class HeaderTokenExtractor {
    public static final String HEADER_PREFIX = "Bearer ";

    public String extract(String header) throws InvalidJwtException {
        if (StringUtils.isEmpty(header) | header.length() < HEADER_PREFIX.length()) {
            throw new InvalidJwtException("올바른 토큰 정보가 아닙니다.");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}

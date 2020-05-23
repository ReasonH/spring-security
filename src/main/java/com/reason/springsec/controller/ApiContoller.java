package com.reason.springsec.controller;

import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.tokens.PostAuthorizationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiContoller {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER')") // SPEL exp lang, user role을 소유해야만 메소드 진입 가능
    public String getUsername(Authentication authentication) {
        PostAuthorizationToken token = (PostAuthorizationToken)authentication;
        return token.getAccountContext().getUsername();
    }
}

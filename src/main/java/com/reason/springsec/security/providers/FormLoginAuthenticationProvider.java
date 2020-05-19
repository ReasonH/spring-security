package com.reason.springsec.security.providers;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.AccountRepository;
import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.AccountContextService;
import com.reason.springsec.security.tokens.PostAuthorizationToken;
import com.reason.springsec.security.tokens.PreAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthorizationToken token = (PreAuthorizationToken)authentication;

        String username = token.getUsername();
        String password = token.getUserPassword();

        Account account = accountRepository.findByUserId(username).orElseThrow(
                () -> new NoSuchElementException("정보에 맞는 계정이 없습니다.")
        );
        if (isCorrectPassword(password, account)){
            return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(account));
        }

        throw new NoSuchElementException("인증 정보가 정확하지 않습니다.");
    }

    // Provider가 Support 할 객체를 명시적으로 밝힌다
    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    public boolean isCorrectPassword(String password, Account account){
        return passwordEncoder.matches(password, account.getPassword()); // 원본이 first parameter
    }
}

package com.reason.springsec.security;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AccountContextService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUserId(username).orElseThrow(() -> new NoSuchElementException("아이디에 맞는 계정이 없습니다."));
        return getAccountContext(account);
    }

    // UserDetails를 구현한 User를 상속받은 AccountContext가 나와서 provider에서 쓸 수 있게됨
    private AccountContext getAccountContext(Account account) {
        return AccountContext.fromAccountModel(account);
    }
}

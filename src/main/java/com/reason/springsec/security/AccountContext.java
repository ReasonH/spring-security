package com.reason.springsec.security;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.UserRole;
import com.reason.springsec.security.tokens.JwtPostProcessingToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import sun.nio.ch.SelectorImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Account Context 생성
public class AccountContext extends User {

    private Account account;
    private AccountContext(Account account, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.account = account;
    }

    public AccountContext(String username, String password, String role){
        super(username, password, parseAuthorities(role));
    }

    public static AccountContext fromAccountModel(Account account){
        return new AccountContext(account, account.getUserId(), account.getPassword(), parseAuthorities(account.getUserRole()));
    }

    // jwtposttoken으로부터 AccountContext 생성
    public static AccountContext fromJwtPostToken(JwtPostProcessingToken token){
        return new AccountContext(null, token.getUserId(), token.getPassword(), token.getAuthorities());
    }

    private static List<SimpleGrantedAuthority> parseAuthorities(UserRole role) {
        return Stream.of(role).map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    private static List<SimpleGrantedAuthority> parseAuthorities(String role) {
        return parseAuthorities(UserRole.getRoleByName(role));
    }

    public Account getAccount(){
        return account;
    }
}

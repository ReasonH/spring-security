package com.reason.springsec.security.providers;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.AccountRepository;
import com.reason.springsec.domain.SocialProviders;
import com.reason.springsec.domain.UserRole;
import com.reason.springsec.dtos.SocialLoginDto;
import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.services.SocialFetchService;
import com.reason.springsec.security.social.SocialUserProperty;
import com.reason.springsec.security.tokens.PostAuthorizationToken;
import com.reason.springsec.security.tokens.SocialPreAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SocialLoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SocialFetchService service;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialPreAuthorizationToken token = (SocialPreAuthorizationToken)authentication;
        SocialLoginDto dto = token.getDto();

        SocialUserProperty property = service.getSocialUserInfo(dto);
        String userId = property.getUserId();
        SocialProviders provider = dto.getProvider();

        return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(getAccount(dto)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialPreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    private Account getAccount(SocialLoginDto dto){
        // 서비스에서 프로퍼티 획득
        SocialUserProperty property = service.getSocialUserInfo(dto);

        // 프로퍼티에서 유저아이디, 프로바이더 획득
        String userId = property.getUserId();
        SocialProviders socialProvider = dto.getProvider();

        // 이를 이용해 검색한다
        return accountRepository.findBySocialIdAndSocialProvider(Long.valueOf(userId), socialProvider)
                .orElseGet(() -> {
                    return accountRepository.save(
                            Account.builder()
                            .id(null)
                            .username(property.getUserNickName())
                            .userId("SOCIAL_USER")
                            .password(String.valueOf(UUID.randomUUID().getLeastSignificantBits()))
                            .userRole(UserRole.USER)
                            .socialId(Long.valueOf(property.getUserId()))
                            .socialProvider(socialProvider)
                            .profileHref(property.getProfileHref())
                            .build()
                    );
                });
    }
}

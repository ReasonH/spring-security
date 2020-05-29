package com.reason.springsec.security.services;

import com.reason.springsec.dtos.SocialLoginDto;
import com.reason.springsec.security.social.SocialUserProperty;

public interface SocialFetchService {
    SocialUserProperty getSocialUserInfo(SocialLoginDto dto);
}

package com.reason.springsec.security.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = KaKaoUserPropertyDeserializer.class)
public class KakaoUserProperty implements SocialUserProperty {
    private Long userId;
    private String userNickName;
    private String profileHref;
    private String email;

    @Override
    public String getUserId() {
        return userId.toString();
    }

    @Override
    public String getUserNickName() {
        return userNickName;
    }

    @Override
    public String getProfileHref() {
        return profileHref;
    }

    @Override
    public String getEmail() {
        return email;
    }
}

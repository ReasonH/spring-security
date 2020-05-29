package com.reason.springsec.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reason.springsec.domain.SocialProviders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 어디에서 전달된 어떤 토큰인지만 갖고있는 dto
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginDto {
    @JsonProperty("provider")
    SocialProviders provider;

    @JsonProperty("token")
    String token;
}

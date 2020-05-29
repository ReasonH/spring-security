package com.reason.springsec.security.services.impl;

import com.reason.springsec.domain.SocialProviders;
import com.reason.springsec.dtos.SocialLoginDto;
import com.reason.springsec.security.services.SocialFetchService;
import com.reason.springsec.security.social.KakaoUserProperty;
import com.reason.springsec.security.social.SocialUserProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// 요청 헤더 생성
@Service
public class SocialFetchServiceProd implements SocialFetchService {
    // 인터페이스 SocialFetchService 에는 sociallogin dto로부터 socialuserproperty를 뽑아오는 함수 정의
    // 여기서는 이를 구현한다.
    private static final String HEADER_PREFIX = "Bearer ";

    @Override
    public SocialUserProperty getSocialUserInfo(SocialLoginDto dto) {
        SocialProviders provider = dto.getProvider();
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("parameter", generateHeader(dto.getToken()));

        // mapping issue
        // provider에 명시된 주소, get function set, 헤더, get으로 받아온 json 매핑할 방법 제공 (metaclass)
        return restTemplate.exchange(provider.getUserInfoEndpoint(), HttpMethod.GET, entity, provider.getPropertyMetaClass()).getBody();
    }
    private HttpHeaders generateHeader(String token){
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", generateHeaderContent(token));

        System.out.println(header);
        return header;
    }

    private String generateHeaderContent(String token){
        StringBuilder sb = new StringBuilder();

        sb.append(HEADER_PREFIX);
        sb.append(token);

        return sb.toString();
    }
}

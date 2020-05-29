package com.reason.springsec.security.services;

import com.reason.springsec.domain.SocialProviders;
import com.reason.springsec.dtos.SocialLoginDto;
import com.reason.springsec.security.services.impl.SocialFetchServiceProd;
import com.reason.springsec.security.social.SocialUserProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.LogFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SocialFetchServiceTest {
    // 내부에 autowired (di) 없고 통합테스트가 아닐 경우 그냥 객체생성해서 테스트하는 것도 방법이다.
    private SocialFetchServiceProd prod = new SocialFetchServiceProd();
    private SocialLoginDto dto;
    private Logger log = LoggerFactory.getLogger(SocialFetchServiceTest.class);

    @BeforeEach
    public void setUp(){
        this.dto = new SocialLoginDto(SocialProviders.KAKAO, "mQh8KNtEAkDwRsu-baye1XNQY80h-8g-rp9boQorDR8AAAFyYSTCNQ");
    }
    @Test
    public void restTemplate_practice1(){
        log.info(new RestTemplate().getForObject("http://www.naver.com", String.class));
    }

    @Test
    public void service_fetchSocialInfo(){
        SocialUserProperty property = prod.getSocialUserInfo(this.dto);
        assertThat(property.getEmail(), is("azsxd34@naver.com"));
    }

}


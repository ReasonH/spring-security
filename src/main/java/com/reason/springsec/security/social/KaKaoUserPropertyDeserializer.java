package com.reason.springsec.security.social;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class KaKaoUserPropertyDeserializer extends StdDeserializer<KakaoUserProperty> {
    public KaKaoUserPropertyDeserializer(){
        this(null);
    }
    protected KaKaoUserPropertyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KakaoUserProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        Long id = jsonNode.get("id").asLong();
        return KakaoUserProperty.builder()
                .userId(jsonNode.get("id").asLong())
                .email(jsonNode.path("kakao_account").get("email").asText())
                .profileHref(jsonNode.path("kakao_account").path("profile").get("profile_image_url").asText())
                .userNickName(jsonNode.path("kakao_account").path("profile").get("nickname").asText())
                .build();
    }
}

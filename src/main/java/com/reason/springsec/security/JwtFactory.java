package com.reason.springsec.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.reason.springsec.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class JwtFactory {

    private static final Logger log = LoggerFactory.getLogger(JwtFactory.class);
    private static String signingKey = "jwttest";

    // 어카운트 컨텍스트에서 롤만 빼와서 클레임셋에 넣어 토큰을 만든다
    // 디비에 접근하지 않고도 이 유저가 인증 가능한 유저인지, 인증가능하다면 어떤 role을 가지는지를 알 수 있음
    public String generateToken(AccountContext context){
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("owner")
                    .withClaim("USERNAME", context.getAccount().getUserId())
                    .withClaim("USER_ROLE", context.getAccount().getUserRole().getRoleName())
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return token;
    }

    private Algorithm generateAlgorithm() throws UnsupportedEncodingException {
        return Algorithm.HMAC256(signingKey);
    }
}

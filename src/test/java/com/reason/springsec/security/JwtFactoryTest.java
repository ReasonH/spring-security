package com.reason.springsec.security;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.UserRole;
import com.reason.springsec.security.AccountContext;
import com.reason.springsec.security.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class JwtFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(JwtFactory.class);

    private AccountContext context;

    @Autowired
    private JwtFactory factory;

    @BeforeEach
    public void setUp(){
        Account account = new Account();
        this.context = AccountContext.fromAccountModel(account);
    }

    @Test
    public void TEST_JWT_GENERATE(){
        log.error(factory.generateToken(this.context));
    }
}

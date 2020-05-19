package com.reason.springsec;

import com.reason.springsec.domain.Account;
import com.reason.springsec.domain.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringsecApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecApplication.class, args);
    }

    @Bean
    CommandLineRunner bootstrapTestAccount(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        return args -> {
            Account account = new Account();
            account.setPassword(passwordEncoder.encode("1234"));
            accountRepository.save(account);
        };
    } // spring command line runner 는 무조건 한 번 실행
}

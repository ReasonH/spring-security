package com.reason.springsec.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.reason.springsec.security.filters.FilterSkipMatcher;
import com.reason.springsec.security.filters.FormLoginFilter;
import com.reason.springsec.security.filters.JwtAuthenticationFilter;
import com.reason.springsec.security.filters.SocialLoginFilter;
import com.reason.springsec.security.handlers.FormLoginAuthenticationSuccessHandler;
import com.reason.springsec.security.handlers.JwtAuthenticationFailureHandler;
import com.reason.springsec.security.jwt.HeaderTokenExtractor;
import com.reason.springsec.security.providers.FormLoginAuthenticationProvider;
import com.reason.springsec.security.providers.JwtAuthenticationProvider;
import com.reason.springsec.security.providers.SocialLoginAuthenticationProvider;
import com.reason.springsec.security.social.KaKaoUserPropertyDeserializer;
import com.reason.springsec.security.social.KakaoUserProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;

    @Autowired
    private FormLoginAuthenticationProvider provider;

    @Autowired
    private JwtAuthenticationProvider jwtProvider;

    @Autowired
    private SocialLoginAuthenticationProvider socialProvider;

    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Autowired
    private HeaderTokenExtractor headerTokenExtractor;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(KakaoUserProperty.class, new KaKaoUserPropertyDeserializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception    {
        return super.authenticationManagerBean();
    }

    protected FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter filter = new FormLoginFilter("/formlogin", formLoginAuthenticationSuccessHandler, null);
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    protected JwtAuthenticationFilter jwtFilter() throws Exception {
        FilterSkipMatcher matcher = new FilterSkipMatcher(Arrays.asList("/social", "/formlogin"), "/api/**");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtAuthenticationFailureHandler, headerTokenExtractor);
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    protected SocialLoginFilter socialFilter() throws Exception{
        SocialLoginFilter filter = new SocialLoginFilter("/social", formLoginAuthenticationSuccessHandler);
        filter.setAuthenticationManager(super.authenticationManager());

        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.provider)
                .authenticationProvider(this.socialProvider)
                .authenticationProvider(this.jwtProvider);
    }

    @Override // 필터 등록
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 어떤 경우에도 세션을 만들지 않음
        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers("/h2-console").permitAll();
        http.addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(socialFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
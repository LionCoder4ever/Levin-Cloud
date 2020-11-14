package com.lc.config;

import com.lc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * 验证码登陆配置
 */
@Component
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity builder) {
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider(userService);
        // 通过AuthenticationRegistry注入SmsCodeAuthenticationProvider
        builder.authenticationProvider(smsCodeAuthenticationProvider);
    }
}


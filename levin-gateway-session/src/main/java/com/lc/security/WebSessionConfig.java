package com.lc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.server.session.HeaderWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

/**
 * @date 2021/4/12
 * @description 1.
 */
@Configuration
public class WebSessionConfig {

    @Bean
    public WebSessionIdResolver headWebSessionIdResolver() {
        return new HeaderWebSessionIdResolver();
    }

    @Bean
    public ServerSecurityContextRepository webSessionServerSecurityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }
}

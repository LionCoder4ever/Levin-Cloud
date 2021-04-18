package com.lc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @date 2021/4/16
 * @description 1.
 */
public class SmsFormLoginConverter extends  SmsFormLoginAuthenticationConverter implements ServerAuthenticationConverter {
    public SmsFormLoginConverter() {
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return this.apply(exchange);
    }
}

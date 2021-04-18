package com.lc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * operation on session
 */
@Component
@Slf4j
public class SessionFilter implements GlobalFilter, Ordered {


    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("this is a pre filter");
        return exchange.getSession().flatMap(webSession -> {
            log.info("session is started ****** {}", webSession.isStarted());
            log.info("session is expired ****** {}", webSession.isExpired());
            if (!webSession.isStarted() || webSession.isExpired()) {
                ServerHttpRequest build = exchange.getRequest().mutate().header("x-auth-token", webSession.getId()).build();
                exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
                return exchange.getResponse().setComplete();
            }
            log.info("current web session: {}", webSession.getId());
            webSession.getAttributes().put(webSession.getId(), "test content");
            ServerHttpRequest build = exchange.getRequest().mutate().header("x-auth-token", webSession.getId()).build();
            return chain.filter(exchange.mutate().request(build).build());
        }).then(Mono.fromRunnable(() -> {
            log.info("this is a post filter");
        }));
    }
}

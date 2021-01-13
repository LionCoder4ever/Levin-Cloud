package com.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator accountRouteLocator(RouteLocatorBuilder builder) {
       return builder.routes()
           .route("account-service",r->r.path("/account/**")
                   .filters(gatewayFilterSpec -> gatewayFilterSpec.stripPrefix(1))
                   .uri("lb://account")
           )
           .build();
    }

    @Bean
    public RouteLocator authRouteLocator1(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service",r->r.path("/auth/**")
                    .filters(gatewayFilterSpec -> gatewayFilterSpec.stripPrefix(1))
                    .uri("lb://auth")
            )
            .build();
    }

}

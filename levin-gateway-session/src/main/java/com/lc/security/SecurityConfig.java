package com.lc.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @date 2021/4/15
 * @description webflux spring security
 */

@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private ServerSecurityContextRepository serverSecurityContextRepository;

    private static final String loginPath = "login";

    private static final String usernameparameter = "username";

    private static final String passwordparameter = "password";

    private static final String phoneparameter = "phone";

    private static final String smscodeparameter = "smscode";


    ServerAuthenticationSuccessHandler authenticationSuccessHandler = new ServerAuthenticationSuccessHandler() {
        @Override
        public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
            return webFilterExchange.getExchange().getSession().flatMap(session -> {
                ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                response.setStatusCode(HttpStatus.OK);
                DataBuffer buffer = response.bufferFactory().wrap(session.getId().getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            });
        }
    };
    ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationFailureHandler() {
        @Override
        public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.NOT_IMPLEMENTED);
            DataBuffer buffer = response.bufferFactory().wrap(("用户认证失败，网关返回错误提示" + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    };
    /**
     *
     */
    ServerAuthenticationEntryPoint serverAuthenticationEntryPoint = new ServerAuthenticationEntryPoint() {
        @Override
        public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NOT_IMPLEMENTED);
            DataBuffer buffer = response.bufferFactory().wrap(("用户认证失败，网关返回错误提示" + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    };
    /**
     * authorization failed
     */
    ServerAccessDeniedHandler accessDeniedHandler = new ServerAccessDeniedHandler() {
        @Override
        public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NOT_IMPLEMENTED);
            DataBuffer buffer = response.bufferFactory().wrap(("用户权限认证失败，网关返回错误提示" + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    };

    /**
     * passwordEncode bean, e.g. use BCryptPasswordEncoder in production mode
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * delegatingReactive managers
     *
     * @return
     */
    @Deprecated
    protected ReactiveAuthenticationManager managers() {
        return new DelegatingReactiveAuthenticationManager(smsAuthenticationManager(), pwdAuthenticationManager());
    }

    /**
     * pwd authenticationManager withe mock user repo
     *
     * @return
     */
    protected ReactiveAuthenticationManager pwdAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(passwordEncoder());
        return userDetailsRepositoryReactiveAuthenticationManager;
    }

    /**
     * sms authenticationManager withe mock user repo
     *
     * @return
     */
    protected ReactiveAuthenticationManager smsAuthenticationManager() {
        return new SmsReactiveAuthenticationManager(smsUserDetailService());
    }

    /**
     * mock userDetail service e.g. use mysql, mongo backend
     *
     * @return
     */
    protected ReactiveUserDetailsService userDetailsService() {
        UserDetails build = User.withUsername("java").password("test").roles("admin").build();
        return new MapReactiveUserDetailsService(build);
    }

    /**
     * mock smsCode service, e.g. create SmsReactiveUserDetailService extend ReactiveUserDetailsService and add findUserByPhone function
     *
     * @return
     */
    protected ReactiveUserDetailsService smsUserDetailService() {
        UserDetails sms = User.withUsername("sms").password("123").roles("user").build();
        return new MapReactiveUserDetailsService(sms);
    }

    /**
     * divide the route path and the spring security path
     * if path need to route to other micro service, return notMatch, the authentication filters will not handle the request
     *
     * @Return
     */
    private ServerWebExchangeMatcher createLoginMatcher(String principal, String credential) {
        return exchange -> {
            RequestPath path = exchange.getRequest().getPath();
            PathContainer.Element element = path.elements().get(1);
            log.info("path : {}", element.value());
            Mono<MultiValueMap<String, String>> formData = exchange.getFormData();
            return formData.flatMap(map -> {
                if (loginPath.equals(element.value()) && map.containsKey(principal) && map.containsKey(credential)) {
                    return ServerWebExchangeMatcher.MatchResult.match();
                }
                return Mono.empty();
            }).switchIfEmpty(ServerWebExchangeMatcher.MatchResult.notMatch());
        };
    }

    private ServerWebExchangeMatcher pwdLoginMatcher() {
        return createLoginMatcher(usernameparameter, passwordparameter);
    }

    private ServerWebExchangeMatcher smsLoginMatcher() {
        return createLoginMatcher(phoneparameter, smscodeparameter);
    }

    /**
     * create AuthenticationWebFilter
     * @param name
     * @param manager
     * @param converter
     * @param successHandler
     * @param failureHandler
     * @param matcher
     * @param securityContextRepository
     * @return
     */
    private AuthenticationWebFilter createAuthenticationWebFilter(String name,
                                                                  ReactiveAuthenticationManager manager,
                                                                  ServerAuthenticationConverter converter,
                                                                  ServerAuthenticationSuccessHandler successHandler,
                                                                  ServerAuthenticationFailureHandler failureHandler,
                                                                  ServerWebExchangeMatcher matcher,
                                                                  ServerSecurityContextRepository securityContextRepository) {
        log.info("laod {} filter", name);
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        if (null != converter) authenticationWebFilter.setServerAuthenticationConverter(converter);
        authenticationWebFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationWebFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationWebFilter.setRequiresAuthenticationMatcher(matcher);
        authenticationWebFilter.setSecurityContextRepository(securityContextRepository);
        return authenticationWebFilter;
    }

    /**
     * username + password login
     *
     * @return
     */
    public AuthenticationWebFilter pwdAuthenticationWebFilter() {
        return createAuthenticationWebFilter("pwdFilter", pwdAuthenticationManager(), new ServerFormLoginAuthenticationConverter(),
                authenticationSuccessHandler, authenticationFailureHandler, pwdLoginMatcher(), serverSecurityContextRepository);
    }

    /**
     * phone + sms code login
     *
     * @return
     */
    public AuthenticationWebFilter smsAuthenticationWebFilter() {
        return createAuthenticationWebFilter("smsFilter", smsAuthenticationManager(), new SmsFormLoginConverter(),
                authenticationSuccessHandler, authenticationFailureHandler, smsLoginMatcher(), serverSecurityContextRepository);
    }

    /**
     * spring security configuration
     * add filter handle custom login e.g. sms code
     *
     * @param
     * @return
     */
    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.csrf().disable().formLogin().disable()
                .authorizeExchange().anyExchange().hasRole("admin").and()
                .addFilterAfter(pwdAuthenticationWebFilter(), SecurityWebFiltersOrder.REACTOR_CONTEXT)
                .addFilterBefore(smsAuthenticationWebFilter(), SecurityWebFiltersOrder.FORM_LOGIN)
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(serverAuthenticationEntryPoint))
                .build();
    }
}

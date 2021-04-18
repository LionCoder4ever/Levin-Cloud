package com.lc.security;

import com.lc.model.SmsAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * @date 2021/4/16
 * @description 1.
 */
public class SmsFormLoginAuthenticationConverter implements
        Function<ServerWebExchange, Mono<Authentication>> {

    private String phoneParameter = "phone";

    private String smsCodeParameter = "smscode";

    @Override
    public Mono<Authentication> apply(ServerWebExchange exchange) {
        return exchange.getFormData()
                .map(data -> createAuthentication(data)).filter(i -> !Objects.isNull(i.getCredentials()) && !Objects.isNull(i.getPrincipal()));
    }

    private Authentication createAuthentication(
            MultiValueMap<String, String> data) {
        for (String item: Arrays.asList(phoneParameter, smsCodeParameter)
             ) {
            if (!data.containsKey(item)) {
                return new SmsAuthenticationToken(null,null);
            }
        }
        String username = data.getFirst(this.phoneParameter);
        String password = data.getFirst(this.smsCodeParameter);
        return new SmsAuthenticationToken(username, password);
    }

    /**
     * The parameter name of the form data to extract the username
     * @param phoneParameter the phone HTTP parameter
     */
    public void setPhoneParameter(String phoneParameter) {
        Assert.notNull(phoneParameter, "usernameParameter cannot be null");
        this.phoneParameter = phoneParameter;
    }

    /**
     * The parameter name of the form data to extract the smsCode
     * @param smsCodeParameter the password HTTP parameter
     */
    public void setSmsCodeParameter(String smsCodeParameter) {
        Assert.notNull(smsCodeParameter, "passwordParameter cannot be null");
        this.smsCodeParameter = smsCodeParameter;
    }
}


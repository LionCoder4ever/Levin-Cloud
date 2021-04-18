package com.lc.security;

import com.lc.model.SmsAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @date 2021/4/16
 * @description 1.
 */
public class SmsReactiveAuthenticationManager extends AbstractUserDetailsReactiveAuthenticationManager {
    private ReactiveUserDetailsService userDetailsService;

    public SmsReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Mono<UserDetails> retrieveUser(String username) {
        return this.userDetailsService.findByUsername(username);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String code = Objects.isNull(authentication.getCredentials()) ? "" : (String)authentication.getCredentials();
        return this.retrieveUser(authentication.getName())
                .filter(u -> StringUtils.equals(code, u.getPassword()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("error sms code"))))
                .map(u -> new SmsAuthenticationToken(u, u.getPassword(), u.getAuthorities()) );
    }

}

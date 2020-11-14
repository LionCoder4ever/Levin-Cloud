package com.lc.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码授权器,参考ResourceOwnerPasswordTokenGranter
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "sms_code";

    private final AuthenticationManager authenticationManager;

    public SmsCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected SmsCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new HashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");

        Authentication userAuth = new SmsCodeAuthenticationToken(mobile);

        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ex) {
            throw new InvalidGrantException(ex.getMessage());
        }

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedoauth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedoauth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate mobile: " + mobile);
        }
    }
}


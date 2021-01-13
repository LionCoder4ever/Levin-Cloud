package com.lc.config;

import com.lc.domain.SecurityUser;
import com.lc.model.DTO.UserDTO;
import com.lc.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码登陆校验器
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public SmsCodeAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) smsCodeAuthenticationToken.getPrincipal();
        //校验手机号验证码
        validSmsCode();
        //fegin加载用户信息
        UserDTO userDTO = userService.loadUserByPhone(mobile);
        if(null == userDTO){
            throw new BadCredentialsException("手机号或验证码错误");
        }
        SecurityUser user = new SecurityUser(userDTO);
        return new SmsCodeAuthenticationToken(user, user.getAuthorities());
    }

    /**
     * 校验验证码
     */
    private void validSmsCode() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取验证码
        String phone = request.getParameter("mobile");
        String smsCode = request.getParameter("smsCode");
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsCode)) {
            throw new BadCredentialsException("手机号或验证码错误");
        }
    }

    /**
     * ProviderManager 授权前判断
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

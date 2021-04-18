package com.lc.controller;

import cn.hutool.json.JSONObject;
import com.lc.api.CommonResult;
import com.lc.model.DTO.UserDTO;
import com.lc.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@Slf4j
@RequestMapping("/account")
@Api(tags = "账号")
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/loadUserByName")
    public UserDTO userInfoByName(@RequestParam("name") String name){
        log.info("auth request load user");
        return userService.loadUserByName(name);
    }

    @PostMapping("/loadUserByPhone")
    public UserDTO userInfoByPhone(@RequestParam("phone") String phone){
        log.info("auth request load user by sms");
        return userService.loadUserByPhone(phone);
    }

    @PostMapping("/user/info")
    public CommonResult info(@RequestHeader("user") String userString) {
        log.info(userString);
        JSONObject user = new JSONObject(userString);
        String userId = user.getStr("id");
        if (StringUtils.isEmpty(userId)) {
            return CommonResult.failed("");
        }
        return CommonResult.success(userService.loadUserById(userId));
    }

    /**
     * test spring gateway webflux session
     * @param request
     * @return
     */
    @PostMapping("/user/test")
    public CommonResult info(HttpServletRequest request)  {
        Object spring_security_context = request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        log.info(spring_security_context.toString());
        return CommonResult.success("ok");
    }

}

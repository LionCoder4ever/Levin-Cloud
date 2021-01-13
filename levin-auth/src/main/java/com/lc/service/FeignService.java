package com.lc.service;

import com.lc.model.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "account")
public interface FeignService {

    @PostMapping(value = "/account/loadUserByName")
    UserDTO loadUserByName(@RequestParam String name);

    @PostMapping(value = "/account/loadUserByPhone")
    UserDTO loadUserByPhone (@RequestParam String phone);

}

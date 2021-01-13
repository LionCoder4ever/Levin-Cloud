package com.lc.service.impl;

import com.lc.model.DTO.UserDTO;
import com.lc.service.FeignService;
import com.lc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private FeignService feignService;

    @Override
    public UserDTO loadUserByName(String name) {
        return feignService.loadUserByName(name);
    }
    @Override
    public UserDTO loadUserByPhone(String name) {
        return feignService.loadUserByPhone(name);
    }

}

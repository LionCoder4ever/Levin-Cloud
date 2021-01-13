package com.lc.service;

import com.lc.model.DTO.UserDTO;

public interface UserService {

    UserDTO loadUserByName (String name);

    UserDTO loadUserByPhone (String phone);
}

package com.lc.service.impl;

import com.lc.dao.UserDTORepository;
import com.lc.model.DTO.UserDTO;
import com.lc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDTORepository userDTORepository;

    @Override
    public UserDTO loadUserByName(String name) {
        return userDTORepository.findFirstByUsername(name);
    }

    @Override
    public UserDTO loadUserByPhone(String phone) {
        return userDTORepository.findFirstByPhone(phone);
    }

    @Override
    public UserDTO loadUserById(String id) {
        return userDTORepository.findUserDTOById(id);
    }

}

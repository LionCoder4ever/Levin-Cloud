package com.lc.dao;

import com.lc.model.DTO.UserDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDTORepository extends MongoRepository<UserDTO, String> {
    UserDTO findFirstByUsername(String username);

    UserDTO findFirstByPhone(String phone);

    UserDTO findUserDTOById(String id);
}

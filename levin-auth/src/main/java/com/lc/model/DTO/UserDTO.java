package com.lc.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private String id;
    private String username;
    private String password;
    private Boolean isActive ;
    private List<String> roles;
    private String phone;
}

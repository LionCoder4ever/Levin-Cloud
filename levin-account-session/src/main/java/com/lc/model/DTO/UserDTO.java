package com.lc.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="users")
public class UserDTO {
    @Id
    private String id;
    private String username;
    private String password;
    private Boolean isActive ;
    private String[] roles;
    private String phone;
}

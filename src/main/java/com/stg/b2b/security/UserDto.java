package com.stg.b2b.security;

import com.stg.b2b.entity.Role;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private  int userId;
    private String userDomainId;
    private Role role;

}

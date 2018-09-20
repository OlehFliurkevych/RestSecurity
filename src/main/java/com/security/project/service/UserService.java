package com.security.project.service;

import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;

public interface UserService {

    RestMessageDTO createAccountForUser(UserDTO userDto);
    RestMessageDTO<UserDTO> findUserByEmail(String email);
}

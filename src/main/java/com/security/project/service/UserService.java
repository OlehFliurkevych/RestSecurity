package com.security.project.service;

import com.security.project.dto.AuthUserDTO;
import com.security.project.dto.LoginDTO;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;

public interface UserService {

    RestMessageDTO createNotEnabledUser(UserDTO registrationDTO);

    AuthUserDTO authenticateUser(LoginDTO loginUserDTO);

    AuthUserDTO getLoginUser();

    RestMessageDTO sendMessageToUser(String mail);
}

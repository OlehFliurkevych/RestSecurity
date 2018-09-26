package com.security.project.service;

import com.security.project.dto.AuthUserDTO;
import com.security.project.dto.LoginDTO;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

public interface UserService {

    RestMessageDTO createNotEnabledUser(UserDTO registrationDTO);

    AuthUserDTO authenticateUser(LoginDTO loginUserDTO);

    AuthUserDTO getLoginUser();

    RestMessageDTO getVerification(String token);

//    RestMessageDTO sendMessageToUser(String mail);
}

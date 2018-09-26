package com.security.project.controller;

import com.security.project.dto.AuthUserDTO;
import com.security.project.dto.LoginDTO;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import com.security.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/public/user/sign-up", method = RequestMethod.POST)
    public RestMessageDTO singUp(@RequestBody UserDTO registrationDTO) {
        return userService.createNotEnabledUser(registrationDTO);
    }


    @RequestMapping(value = "/public/user/login", method = RequestMethod.POST)
    public AuthUserDTO login(@RequestBody LoginDTO loginUserDTO) {
        return userService.authenticateUser(loginUserDTO);
    }

    @RequestMapping(value = "/private/user/logout", method = RequestMethod.GET)
    public boolean logout() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(null);
        return true;
    }

    @RequestMapping(value = "/public/user/is-authenticated", method = RequestMethod.GET)
    public AuthUserDTO isUserAuthenticated() {
        return userService.getLoginUser();
    }




//    @RequestMapping(value="/public/user/send/",method = RequestMethod.GET)
//    public RestMessageDTO sendEmailToUser(@RequestParam("email")String email){
//        return userService.sendMessageToUser(email);
//    }


}

package com.security.project.controller;

import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import com.security.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController  {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/public/user/register",method= RequestMethod.POST)
    public RestMessageDTO registerNewUser(@Valid @RequestBody UserDTO userDto){
        return userService.createAccountForUser(userDto);
    }

    @RequestMapping(value="/user",method = RequestMethod.GET)
    public RestMessageDTO<UserDTO> getUserByEmail(@RequestParam("email")String email){
        return userService.findUserByEmail(email);
    }
}

package com.security.project.controller;

import com.security.project.dto.RestMessageDTO;
import com.security.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BaseController {

    @Autowired
    private UserService userService;


    @RequestMapping(value="/verify/",method = RequestMethod.GET)
    public String verify(@RequestParam("token")String token){
        userService.getVerification(token);
        return "verification";
    }
}

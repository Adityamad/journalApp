package com.madabad.journalApp.controller;

import com.madabad.journalApp.entity.User;
import com.madabad.journalApp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserServices userServices;

    @GetMapping("/check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userServices.saveNewUser(user);

    }
}

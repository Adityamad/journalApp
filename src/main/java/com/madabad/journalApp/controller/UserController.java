package com.madabad.journalApp.controller;

import com.madabad.journalApp.entity.JournalEntry;
import com.madabad.journalApp.entity.User;
import com.madabad.journalApp.repository.UserRepository;
import com.madabad.journalApp.services.JournalEntryServices;
import com.madabad.journalApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
    UserServices userServices;

   @Autowired
    UserRepository userRepository;
//
//   @GetMapping
//   public List<User> getAllUsers(){
//
//       return userServices.getAll();
//
//   }



   @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){

       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
       String userName= authentication.getName();
       User userInDb = userServices.findByUserName(userName);
           userInDb.setUserName(user.getUserName());
           userInDb.setPassword(user.getPassword());
           userServices.saveNewUser(userInDb);

       return new ResponseEntity<>(HttpStatus.NO_CONTENT);


   }


   @DeleteMapping
    public ResponseEntity<?> deleteUserByID(){

       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
       userRepository.deleteByUserName(authentication.getName());

       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }
}

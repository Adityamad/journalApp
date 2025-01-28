package com.madabad.journalApp.services;

import com.madabad.journalApp.entity.User;
import com.madabad.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public boolean saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));

            userRepository.save(user);
            return true;
        }catch(Exception e){
            log.error("error occurred for {} :", user.getUserName(),e);

            return false;
        }

    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));

        userRepository.save(user);

    }

    public void saveUser(User user){



        userRepository.save(user);

    }

    public List<User> getAll(){

        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId Id){
        return userRepository.findById(Id);
    }

    public void deleteByID(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);

    }
}

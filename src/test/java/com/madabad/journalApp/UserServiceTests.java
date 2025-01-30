package com.madabad.journalApp;

import com.madabad.journalApp.repository.UserRepository;
import com.madabad.journalApp.services.UserServices;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testFindByUserName(){
        assertNotNull(userRepository.findByUserName("Adi"));
    }
}

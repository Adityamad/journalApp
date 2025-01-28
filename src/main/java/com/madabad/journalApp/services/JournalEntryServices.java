package com.madabad.journalApp.services;

import com.madabad.journalApp.entity.JournalEntry;
import com.madabad.journalApp.entity.User;
import com.madabad.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryServices {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserServices userServices;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userServices.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntry().add(saved);
           // user.setUserName(null); //for checking the functionality of @transactional
            userServices.saveUser(user);
        }  catch(Exception e){

            throw new RuntimeException("an error occurred  while saving the entry",e);
            //log.error("Exception", e);

        }

    }

    public void saveEntry(JournalEntry journalEntry){

            journalEntryRepository.save(journalEntry);


        }


    public List<JournalEntry> getAll(){

        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId Id){
        return journalEntryRepository.findById(Id);
    }

    @Transactional
    public boolean deleteByID(ObjectId id , String userName){

        boolean removed=false;

        try{
            User user = userServices.findByUserName(userName);
            removed = user.getJournalEntry().removeIf( x -> x.getId().equals(id));

            if(removed){
                userServices.saveNewUser(user);

                journalEntryRepository.deleteById(id);
            }
        }catch(Exception e){
            log.error("Error",e);
            throw new RuntimeException("An error occurred while deleting the entry",e);


        }

        return removed;

    }


}

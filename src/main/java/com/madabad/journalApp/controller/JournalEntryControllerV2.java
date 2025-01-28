package com.madabad.journalApp.controller;

import com.madabad.journalApp.entity.JournalEntry;
import com.madabad.journalApp.entity.User;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryServices journalEntryServices;

    @Autowired
    private UserServices userServices;

    @GetMapping()
    public  ResponseEntity<?> getAllJournalEntriesOfUser() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user = userServices.findByUserName(userName);

        List<JournalEntry> all= user.getJournalEntry();

        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {

        try {

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String userName= authentication.getName();

            journalEntryServices.saveEntry(myEntry , userName);
            return new ResponseEntity<>(myEntry ,HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myId) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user= userServices.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntry().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry =journalEntryServices.getById(myId);

            if (journalEntry.isPresent())
            {
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

        boolean removed = journalEntryServices.deleteByID(myId,userName);

        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }


    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId,
                                             @RequestBody JournalEntry newEntry
                                            ) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user= userServices.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntry().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryServices.getById(myId);

            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();

                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());

                journalEntryServices.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }

        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);



    }

}

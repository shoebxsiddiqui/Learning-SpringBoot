package com.journal.demo.service;

import com.journal.demo.entity.User;
import com.journal.demo.entity.journalEntity;
import com.journal.demo.repository.journalRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class journalServices {
    @Autowired
    private journalRepository journalRepository;
    @Autowired private userServices userServices;

    @Transactional
    public void saveEntry(journalEntity myEntry, String userName) {
        try {
            User user = userServices.findByUserName(userName);
            myEntry.setUserName(userName);
            myEntry.setDate(LocalDateTime.now());
            journalEntity saved = journalRepository.save(myEntry);
            user.getJournalEntries().add(saved);
            userServices.saveUser(user);
        } catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<journalEntity> getAllEntries(String userName) {
        return userServices.findByUserName(userName).getJournalEntries();
    }

    public Optional<journalEntity> getEntry(ObjectId id) {
        return journalRepository.findById(id);
    }

    public void deleteEntry(ObjectId id) {
        journalRepository.deleteById(id);
    }

    public void updateEntry(journalEntity entry) {
        journalRepository.save(entry);
    }
}

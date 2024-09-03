package com.journal.demo.Controller;

import com.journal.demo.entity.User;
import com.journal.demo.entity.journalEntity;
import com.journal.demo.service.journalServices;
import com.journal.demo.service.userServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;

@RestController
@RequestMapping("/api/v1/journals")
public class journalController {

    @Autowired private journalServices journalServices;
    @Autowired private userServices userServices;

    @PostMapping
    public ResponseEntity<journalEntity> createJournalBYUsername(@RequestBody journalEntity myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalServices.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getEntriesByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<journalEntity> all = journalServices.getAllEntries(userName);
        return all==null || all.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<journalEntity> getEntry(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUserName(userName);
        journalEntity journalEntity = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElse(null);

        return journalEntity==null ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(journalEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userServices.findByUserName(userName);
            user.getJournalEntries().stream()
                    .filter(x -> x.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Not Found"));
            journalServices.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<journalEntity> updateEntry(@RequestBody journalEntity newEntry, @PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUserName(userName);
        journalEntity oldEntry = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(oldEntry == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(!newEntry.getTitle().isEmpty()) oldEntry.setTitle(newEntry.getTitle());
        if(newEntry.getContent() != null && !newEntry.getContent().isEmpty()) oldEntry.setContent(newEntry.getContent());

        journalServices.updateEntry(oldEntry);
        return new ResponseEntity<>(oldEntry, HttpStatus.OK);
    }
}

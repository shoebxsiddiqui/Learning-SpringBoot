package com.journal.demo.Controller;

import com.journal.demo.entity.User;
import com.journal.demo.service.userServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public")
public class publicController {
    @Autowired
    private userServices userServices;

    @GetMapping("/health")
    public String healthCheck() {
        return "Life...";
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            user.setDate(LocalDateTime.now());
            user.getRoles().add("USER");
            userServices.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

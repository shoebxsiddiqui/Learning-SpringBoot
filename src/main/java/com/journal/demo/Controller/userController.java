package com.journal.demo.Controller;

import com.journal.demo.entity.User;
import com.journal.demo.service.userServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class userController {

    @Autowired private userServices userServices;

    @GetMapping
    public ResponseEntity<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUserName(userName);
        return user==null ? new ResponseEntity<>(HttpStatus.NOT_FOUND): new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User newUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findByUserName(userName);
        user.setUserName(newUser.getUserName());
        user.setPassword(newUser.getPassword());
        userServices.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

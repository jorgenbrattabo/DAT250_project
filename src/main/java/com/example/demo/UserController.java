package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import com.example.demo.domain.User;

@CrossOrigin
@RestController 
public class UserController {

    @Autowired
    private PollManager pollManager;

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        long newId = pollManager.getUsers().size() + 1; // Generate a unique ID
        user.setId(newId);
        pollManager.getUsers().put(newId, user);
        return user; // Return the created user
    }
    
    @GetMapping("/users")
    public Collection<User> listUsers() {
        return pollManager.getUsers().values();
    }
}

    

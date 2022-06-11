package com.example.demo;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @Autowired
    UserService userService;
    //creates a new user with the given email, password, and usertype
    //checks if the user already exists
    @GetMapping("/createUser")
    public String createUser(@RequestBody User user ) throws InterruptedException, ExecutionException, FirebaseAuthException {
        return userService.addUser(user);
    }

    //fetches the user from the database
    //returns null object if the user does not exist
    @GetMapping("/getUser")
    public User getUser(@RequestBody String email) throws InterruptedException, ExecutionException {
        return userService.getUser(email);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws FirebaseAuthException, ExecutionException, InterruptedException, IOException {

        return userService.userLogin(user.getEmail(),user.getPassword());
    }
    @GetMapping("/getEmailVerificationLink")
    public String getLink(@RequestBody String email) throws FirebaseAuthException {

        return userService.generateEmailLink(email);
    }


    }

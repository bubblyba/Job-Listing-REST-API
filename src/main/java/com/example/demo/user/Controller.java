package com.example.demo.user;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Controller {
    @Autowired
    UserService userService;
    //creates a new user with the given email, password, and usertype
    //checks if the user already exists
    @PostMapping("/createUser")
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
    public Token login(@RequestBody User user) throws FirebaseAuthException, ExecutionException, InterruptedException, IOException {
        if(userService.userLogin(user.getEmail(),user.getPassword()).equals("Login Success")){
            return userService.generateToken(user.getEmail());
        }
        else{
            Token token = new Token();
            token.setUsername("Invalid Credentials");
            return token;

        }

    }
    @GetMapping("/getEmailVerificationLink")
    public String getLink(@RequestBody String email) throws FirebaseAuthException {

        return userService.generateEmailLink(email);
    }
    @PostMapping("/validateAuthToken")
    public AccessToken validateAuthToken(@RequestBody ValidateAuthTokenInput input) throws FirebaseAuthException, ExecutionException, InterruptedException, IOException, ParseException {

        return(userService.validateAuthToken(input.getToken(),input.getUsername()));


    }


    }

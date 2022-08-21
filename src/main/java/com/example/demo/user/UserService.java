package com.example.demo.user;

import com.example.demo.user.User;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.protobuf.Api;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
//CRUD operations

@Service
public class UserService {
    List<Token> authTokens = new ArrayList<>();

    //the name of the column where data is being read from the database
    public static final String columnName = "users";

    private FirebaseAuth mAuth;

    public String addUser(User user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        //checks if the user exists by the email of the user in the parameter
        if (userExists(user.getEmail())) {
            return "User already exists";
        }
        mAuth = FirebaseAuth.getInstance();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        //attempt to add email to user authentication request
        try {
            request.setEmail(user.getEmail());
        } catch (IllegalArgumentException i) {
            return "Invalid Email";
        }
        //attempt to add password to user authentication request
        try {
            request.setPassword(user.getPassword());
        } catch (IllegalArgumentException i) {
            return i.getLocalizedMessage();
        }
        //create user using the email and password
        mAuth.createUser(request);
        //puts all the information in the user object into a Map object
        Map<String, Object> docData = new HashMap<>();
        docData.put("email", user.getEmail());
        docData.put("password", user.getPassword());
        docData.put("userType", user.getUserType());
        // Add a new document in collection "users" with the given email
        dbFirestore.collection(columnName).document(user.getEmail()).set(docData);
        return "User created";
    }

    //returns a User object that matches the unique email given
    public User getUser(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection(columnName).document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            //converts DocumentSnapshot object to a User object
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(document.getData());
            User user = gson.fromJson(jsonElement, User.class);
            return (user);
        } else {
            User user = null;
            return (user);
        }
    }

    //returns an email verification link for the given email
    //does not actually send the email
    public String generateEmailLink(String email) throws FirebaseAuthException {
        mAuth = FirebaseAuth.getInstance();
        String link = mAuth.generateEmailVerificationLink(email);

        return link;
    }

    //checks if the user with the given email already exists
    public boolean userExists(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection(columnName).document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean tokenUserExists(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("authToken").document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //TODO update database to confirm user has logged in
    public String userLogin(String email, String password) throws ExecutionException, InterruptedException {
        mAuth = FirebaseAuth.getInstance();
        if (userExists(email) == false) {
            return "Login Failed Invalid Email or Password";
        } else {
            User user = getUser(email);
            if (user.getPassword().equals(password)) {
                return "Login Success";

            } else {
                return "Login Failed Invalid Email or Password";

            }
        }

    }

    public String generateCustomToken(String email) throws FirebaseAuthException {
        mAuth = FirebaseAuth.getInstance();
        return (mAuth.createCustomToken(email));


    }

    public Token generateToken(String email) throws FirebaseAuthException, ExecutionException, InterruptedException {

        ZoneId zone = ZoneId.of("UTC");

        LocalDateTime localDateTime = LocalDateTime.now(zone);

        Token token = new Token(generateCustomToken(email), "bearer", email, String.valueOf(localDateTime), String.valueOf(LocalDateTime.parse(String.valueOf(localDateTime)).plusMinutes(15)));
        if (tokenUserExists(email)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("expires", String.valueOf(token.getExpires()));
            docData.put("issued", String.valueOf(token.getIssued()));
            docData.put("token", token.getToken());
            docData.put("tokenType", token.getTokenType());
            docData.put("username", token.getUsername());

            // Add a new document in collection "users" with the given email
            dbFirestore.collection("authToken").document(token.getUsername()).update(docData);
        } else {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("expires", String.valueOf(token.getExpires()));
            docData.put("issued", String.valueOf(token.getIssued()));
            docData.put("token", token.getToken());
            docData.put("tokenType", token.getTokenType());
            docData.put("username", token.getUsername());

            // Add a new document in collection "users" with the given email
            dbFirestore.collection("authToken").document(token.getUsername()).set(docData);
        }
        return token;
    }


    public String validateAuthToken(String authToken, String username) throws ExecutionException, InterruptedException, ParseException {
        ArrayList<Token> tokens = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("authToken").get();
// future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            Token toke = new Token();
            Token fireToke = document.toObject(Token.class);
            toke.setUsername(fireToke.getUsername());
            toke.setToken(fireToke.getToken());
            toke.setExpires(fireToke.getExpires());
            toke.setIssued(fireToke.getIssued());
            toke.setTokenType(fireToke.getTokenType());


            tokens.add(toke);
        }
        for (int x = 0; x < tokens.size(); x++) {

            if (tokens.get(x).getUsername().equals(username) && tokens.get(x).getToken().equals(authToken)) {
                ZoneId zone = ZoneId.of("UTC");

                LocalDateTime localDateTime = LocalDateTime.now(zone);



                boolean isAfter = localDateTime.isAfter(LocalDateTime.parse(tokens.get(x).getExpires()));
                System.out.println("time now: " + localDateTime);
                System.out.println("expire time: " + tokens.get(x).getExpires());
                if(isAfter){
                    return "expired";
                }
            }

        }
        return "chillin";

    }




    }







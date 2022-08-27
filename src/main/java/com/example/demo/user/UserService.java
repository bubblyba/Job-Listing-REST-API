package com.example.demo.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
        //puts all the information in the user object into a Map object
        Map<String, Object> docData = new HashMap<>();
        docData.put("email", user.getEmail());
        docData.put("password", user.getPassword());
        docData.put("userType", user.getUserType());
        // Add a new document in collection "users" with the given email
        dbFirestore.collection(columnName).document(user.getEmail()).set(docData);
        Map<String, Object> docData2 = new HashMap<>();
        docData2.put("username", user.getEmail());
        docData2.put("profileCreated", false);
        dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("userStatus").document(user.getEmail()).set(docData2);

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

    public boolean accessTokenUserExists(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("accessToken").document(email);
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
        ArrayList<User> users = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("users").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        String tokenType = "";
        for (QueryDocumentSnapshot document : documents) {
            User user = new User();
            User tempUser = document.toObject(User.class);

            if(tempUser.getEmail().equals(email)){
                if(tempUser.getUserType() == 2){
                    tokenType += "job";
                }
                else if(tempUser.getUserType() == 1){
                    tokenType += "hire";
                }

            }
        }

        Token token = new Token(generateCustomToken(email), tokenType, email, String.valueOf(localDateTime), String.valueOf(LocalDateTime.parse(String.valueOf(localDateTime)).plusMinutes(15)));
        if (tokenUserExists(email)) {
             dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("expires", String.valueOf(token.getExpires()));
            docData.put("issued", String.valueOf(token.getIssued()));
            docData.put("token", token.getToken());
            docData.put("tokenType", token.getTokenType());
            docData.put("username", token.getUsername());

            // Add a new document in collection "users" with the given email
            dbFirestore.collection("authToken").document(token.getUsername()).update(docData);
        } else {
             dbFirestore = FirestoreClient.getFirestore();

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


    public AccessToken validateAuthToken(String authToken, String username) throws ExecutionException, InterruptedException, ParseException, FirebaseAuthException {
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
            System.out.println(fireToke.getUsername());
            System.out.println(fireToke.getToken());


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
                    AccessToken accessToken = new AccessToken();
                    accessToken.setUsername("token expired");
                    return accessToken;
                }
                else{

                    AccessToken accessToken = new AccessToken(tokens.get(x).getUsername(),String.valueOf(generateCustomToken(username)),String.valueOf(generateCustomToken(username + "code")),String.valueOf(localDateTime), String.valueOf(LocalDateTime.parse(String.valueOf(localDateTime)).plusMinutes(15)),tokens.get(x).getTokenType());
                    if (accessTokenUserExists(tokens.get(x).getUsername())) {
                        dbFirestore = FirestoreClient.getFirestore();
//
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("username", accessToken.getUsername());
                        docData.put("refreshToken", accessToken.getRefreshToken());
                        docData.put("accessToken", accessToken.getAccessToken());
                        docData.put("issued", accessToken.getIssued());
                        docData.put("expires", accessToken.getExpires());
                        docData.put("tokenType", accessToken.getTokenType());

                        // Add a new document in collection "users" with the given email
                        dbFirestore.collection("accessToken").document(tokens.get(x).getUsername()).update(docData);
                    } else {
                        dbFirestore = FirestoreClient.getFirestore();
//
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("username", accessToken.getUsername());
                        docData.put("refreshToken", accessToken.getRefreshToken());
                        docData.put("accessToken", accessToken.getAccessToken());
                        docData.put("issued", accessToken.getIssued());
                        docData.put("expires", accessToken.getExpires());
                        docData.put("tokenType", accessToken.getTokenType());

                        // Add a new document in collection "users" with the given email
                        dbFirestore.collection("accessToken").document(tokens.get(x).getUsername()).set(docData);
                    }
                    return accessToken;

                }

            }


        }
        AccessToken accessToken = new AccessToken();
        accessToken.setUsername("invalid credentials");
        return accessToken;

    }
    public String validateAccessToken(String username, String accessToken) throws ExecutionException, InterruptedException, ParseException, FirebaseAuthException {
        ArrayList<AccessToken> tokens = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("accessToken").get();
// future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            AccessToken toke = new AccessToken();
            AccessToken fireToke = document.toObject(AccessToken.class);
            toke.setUsername(fireToke.getUsername());
            System.out.println(fireToke.getUsername());
            toke.setRefreshToken(fireToke.getRefreshToken());
            toke.setAccessToken(fireToke.getAccessToken());
            System.out.println(fireToke.getAccessToken());
            toke.setTokenType(fireToke.getTokenType());
            toke.setExpires(fireToke.getExpires());
            toke.setIssued(fireToke.getIssued());

            tokens.add(toke);
        }

        for(int x =0;x<tokens.size();x++){

            if(String.valueOf(tokens.get(x).getAccessToken()).equals(accessToken)&&String.valueOf(tokens.get(x).getUsername()).equals(username)){
                ZoneId zone = ZoneId.of("UTC");

                LocalDateTime localDateTime = LocalDateTime.now(zone);



                boolean isAfter = localDateTime.isAfter(LocalDateTime.parse(tokens.get(x).getExpires()));

                if(isAfter){
                    AccessToken accessTok = new AccessToken();
                    return ("token expired");

                }
                else{
                    return "token valid";
                }
            }
        }
        return "invalid token";


    }
    public User getUserByAccessToken(String accessToken) throws ExecutionException, InterruptedException {
        ArrayList<AccessToken> accessTokens = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("accessToken").get();
// future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            AccessToken toke = new AccessToken();
            AccessToken fireToke = document.toObject(AccessToken.class);
            toke.setUsername(fireToke.getUsername());
            toke.setAccessToken(fireToke.getAccessToken());


            accessTokens.add(toke);
        }
        for (int x = 0; x < accessTokens.size(); x++) {


            if(accessTokens.get(x).getAccessToken().equals(accessToken)){
                return getUser(accessTokens.get(x).getUsername());
            }
        }
        User user = new User();
        user.setEmail("internal error");
        return user;

    }
    public String getUserProfileCreated(String username, String accessToken) throws ExecutionException, InterruptedException, ParseException, FirebaseAuthException {
        System.out.println(validateAccessToken(username,accessToken));
        if(!(validateAccessToken(username,accessToken).equals("token valid"))){
            return "invalid token";
        }

        ArrayList<UserStatus> statuses = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("userStatus").get();
// future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            UserStatus status = new UserStatus();
            UserStatus fireStatus = document.toObject(UserStatus.class);
            status.setProfileCreated(fireStatus.isProfileCreated());
            status.setUsername(fireStatus.getUsername());



            statuses.add(status);
        }

       for(int x = 0;x<statuses.size();x++){
           if(statuses.get(x).getUsername().equals(username)){

               return String.valueOf(statuses.get(x).isProfileCreated());
           }
       }

        return "internal issue";
    }
    public boolean workerProfileExists(String username) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("workerProfile").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return true;
        } else {
            return false;
        }
    }
    public String addProfile(WorkerProfile profile) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        if(workerProfileExists(profile.username)){

            dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("bio", String.valueOf(profile.getBio()));
            docData.put("displayName", String.valueOf(profile.getDisplayName()));
            docData.put("gender", profile.getGender());
            docData.put("githubUsername", profile.getGithubUsername());
            docData.put("location",profile.getLocation());
            docData.put("nationality", profile.getNationality());
            docData.put("preferredAnnualPay", profile.getPreferredAnnualPay());
            docData.put("preferredMonthlyPay", profile.getPreferredMonthlyPay());
            docData.put("residence", profile.getResidence());
            docData.put("skills", profile.getSkills());
            docData.put("username", profile.getUsername());
            docData.put("website", profile.getWebsite());

            // Add a new document in collection "users" with the given email
            dbFirestore.collection("workerProfile").document(profile.getUsername()).update(docData);
            return "profile created";
        }
        else{
            dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("bio", String.valueOf(profile.getBio()));
            docData.put("displayName", String.valueOf(profile.getDisplayName()));
            docData.put("gender", profile.getGender());
            docData.put("githubUsername", profile.getGithubUsername());
            docData.put("location",profile.getLocation());
            docData.put("nationality", profile.getNationality());
            docData.put("preferredAnnualPay", profile.getPreferredAnnualPay());
            docData.put("preferredMonthlyPay", profile.getPreferredMonthlyPay());
            docData.put("residence", profile.getResidence());
            docData.put("skills", profile.getSkills());
            docData.put("username", profile.getUsername());
            docData.put("website", profile.getWebsite());

            // Add a new document in collection "users" with the given email
            dbFirestore.collection("workerProfile").document(profile.getUsername()).set(docData);
            return "profile created";

        }


    }

    public String updateUserProfileCreated(boolean profileCreated,String username, String accessToken) throws ParseException, ExecutionException, InterruptedException, FirebaseAuthException {
        if(!(validateAccessToken(username, accessToken).equals("token valid"))){
            return "invalid credentials";
        }
        else {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore = FirestoreClient.getFirestore();

            Map<String, Object> docData = new HashMap<>();
            docData.put("profileCreated", profileCreated);
            docData.put("username", username);


            // Add a new document in collection "users" with the given email
            dbFirestore.collection("userStatus").document(username).update(docData);
            return "profile updated";
        }

    }




    }







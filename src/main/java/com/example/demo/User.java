package com.example.demo;
//defines the User class with it's corresponding variables

public class User {
    //unique email used as primary key in the database
    private String email;
    private String password;
    //If userType equals 1 the user is somone who wants to hire
    //If userType equals 2 the user is somone who wants a job
    private int userType;


    public User(){}
    public User(String email, String password, int userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

package com.example.demo.user;

public class WorkerProfile {
    String accessToken;
    String bio;
    String displayName;
    String gender;
    String githubUsername;
    String location;
    String nationality;
    double preferredAnnualPay;
    double preferredMonthlyPay;
    String residence;
    String skills;
    String username;
    String website;

    public WorkerProfile(){}

    public WorkerProfile(String accessToken, String bio, String displayName, String gender, String githubUsername, String location, String nationality, double preferredAnnualPay, double preferredMonthlyPay, String residence, String skills, String username, String website) {
        this.accessToken = accessToken;
        this.bio = bio;
        this.displayName = displayName;
        this.gender = gender;
        this.githubUsername = githubUsername;
        this.location = location;
        this.nationality = nationality;
        this.preferredAnnualPay = preferredAnnualPay;
        this.preferredMonthlyPay = preferredMonthlyPay;
        this.residence = residence;
        this.skills = skills;
        this.username = username;
        this.website = website;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public double getPreferredAnnualPay() {
        return preferredAnnualPay;
    }

    public void setPreferredAnnualPay(double preferredAnnualPay) {
        this.preferredAnnualPay = preferredAnnualPay;
    }

    public double getPreferredMonthlyPay() {
        return preferredMonthlyPay;
    }

    public void setPreferredMonthlyPay(double preferredMonthlyPay) {
        this.preferredMonthlyPay = preferredMonthlyPay;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

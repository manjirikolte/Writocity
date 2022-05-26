package com.example.writocityapplication.models;

public class User {
    public String fullName, email, bio, username, profilePhoto;

    public User(){}
    public User(String email, String username, String fullName, String bio){
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
    }

    public User(String profilePhoto){
        this.profilePhoto = profilePhoto;
    }

    public User(String fullName, String username, String bio){
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

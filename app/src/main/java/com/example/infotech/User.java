package com.example.infotech;

public class User {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String profileImage;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String username, String email, String firstName, String lastName, String middleName, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddlename() {
        return middleName;
    }

    public void setMiddlename(String middleName) {
        this.middleName = middleName;
    }

    public String getProfileImageURL() {
        return profileImage;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImage = profileImageURL;
    }
}

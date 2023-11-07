package com.example.infotech;

public class Friend {
    private String id;
    private String name;
    private String profileImageURL;

    public Friend() {
        // Default constructor required for Firebase
    }

    public Friend(String id, String name, String profileImageURL) {
        this.id = id;
        this.name = name;
        this.profileImageURL = profileImageURL;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}

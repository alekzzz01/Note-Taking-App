package com.example.infotech;

import io.realm.RealmObject;

public class Note extends RealmObject {
    private String title;
    private String description;
    private long createdTime;

    // Required empty constructor
    public Note() {
    }

    // Getters and setters for the fields
    // Make sure to include these methods for Realm to work properly

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
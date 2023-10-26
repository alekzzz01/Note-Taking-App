package com.example.infotech;

public class Note {
    private String title;
    private long time;
    private String description;

    public Note() {
        // Default constructor required for Firebase
    }

    public Note(String title, long time, String description) {
        this.title = title;
        this.time = time;
        this.description = description;
    }

    public String getTitle() {

        return title;
    }

    public long getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}

package com.example.infotech;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private String title;
    private long timestamp;
    private String description;
    private String key;


    public Note() {
        // Default constructor required for Firebase
    }

    public Note(String title, long time, String description) {
        this.title = title;
        this.timestamp = time;
        this.description = description;

    }

    public Note(String key, String title, long time, String description) {
        this.key = key;
        this.title = title;
        this.timestamp = time;
        this.description = description;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }



    // Parcelable implementation
    protected Note(Parcel in) {
        title = in.readString();
        timestamp = in.readLong();
        description = in.readString();
        key = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(timestamp);
        dest.writeString(description);
        dest.writeString(key);
    }
}

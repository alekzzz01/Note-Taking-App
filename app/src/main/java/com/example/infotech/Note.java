package com.example.infotech;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private String title;
    private long time;
    private String description;
    private String key;
    private boolean isSpecial;// Unique key for the note

    public Note() {
        // Default constructor required for Firebase
    }

    public Note(String title, long time, String description) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.isSpecial = isSpecial;
    }

    public Note(String key, String title, long time, String description) {
        this.key = key;
        this.title = title;
        this.time = time;
        this.description = description;
        this.isSpecial = isSpecial;
    }

    public String getTitle() {
        return title;
    }

    public long getTimestamp() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    // Parcelable implementation
    protected Note(Parcel in) {
        title = in.readString();
        time = in.readLong();
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
        dest.writeLong(time);
        dest.writeString(description);
        dest.writeString(key);
    }
}

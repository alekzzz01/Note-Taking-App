package com.example.infotech;

import android.os.Parcel;
import android.os.Parcelable;

public class FlashcardSet implements Parcelable {
    private String title;
    private long flashcardCount;

    public FlashcardSet() {
        // Default constructor required for Firebase
    }

    public FlashcardSet(String title, long flashcardCount) {
        this.title = title;
        this.flashcardCount = flashcardCount;
    }

    protected FlashcardSet(Parcel in) {
        title = in.readString();
        flashcardCount = in.readLong();
    }

    public static final Creator<FlashcardSet> CREATOR = new Creator<FlashcardSet>() {
        @Override
        public FlashcardSet createFromParcel(Parcel in) {
            return new FlashcardSet(in);
        }

        @Override
        public FlashcardSet[] newArray(int size) {
            return new FlashcardSet[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public long getFlashcardCount() {
        return flashcardCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(flashcardCount);
    }
}

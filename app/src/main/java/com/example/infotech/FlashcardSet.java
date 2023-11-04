package com.example.infotech;

public class FlashcardSet {
    private String title;
    private long flashcardCount; // Change the data type to long

    public FlashcardSet() {
        // Default constructor required for Firebase
    }

    public FlashcardSet(String title, long flashcardCount) {
        this.title = title;
        this.flashcardCount = flashcardCount;
    }

    // Rest of the class remains the same


    public String getTitle() {
        return title;
    }

    public int getFlashcardCount() {
        return (int) flashcardCount;
    }
}

package com.example.infotech;

public class FriendRequest {
    private String senderId;
    private String receiverId;
    private boolean accepted;
    private String senderName;
    private String senderEmail; // Add a field to store the sender's email
    private String senderProfileImage; // Add a field to store the sender's profile image

    public FriendRequest() {
        // Default constructor for Firebase
    }

    public FriendRequest(String senderId, String receiverId, String senderName, String senderEmail, String senderProfileImage) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.senderEmail = senderEmail; // Set the sender's email
        this.senderProfileImage = senderProfileImage; // Set the sender's profile image
        this.accepted = false;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}

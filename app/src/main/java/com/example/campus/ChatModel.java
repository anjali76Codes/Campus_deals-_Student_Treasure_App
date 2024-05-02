package com.example.campus;

public class ChatModel {
    private String message;
    private String senderId; // Optionally, you might include the sender's ID
    private long timestamp; // Optionally, you might include the timestamp of the message

    // Constructors, getters, and setters
    public ChatModel() {
        // Default constructor required for Firebase Realtime Database
    }

    public ChatModel(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

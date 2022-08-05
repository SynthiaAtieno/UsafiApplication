package com.example.usafiapp.model;


public class Notification {
    String receiverId, senderId, text, date;

    public Notification() {
    }

    public Notification(String receiverId, String senderId, String text, String date) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.text = text;
        this.date = date;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
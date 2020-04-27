package com.example.messangerapplication.Notification;

public class Data {
    private String user;
    private String title;

    public Data(String user, String title) {
        this.user = user;
        this.title = title;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

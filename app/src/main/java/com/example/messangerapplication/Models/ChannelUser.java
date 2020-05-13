package com.example.messangerapplication.Models;

public class ChannelUser {
    private String name, mail, uid;

    public ChannelUser(){}

    public ChannelUser(String name, String mail, String uid) {
        this.name = name;
        this.mail = mail;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

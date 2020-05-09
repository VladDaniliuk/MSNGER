package com.example.messangerapplication.Models;

public class Share {
    String name,mail,UID;

    public Share(){}

    public Share(String a, String b) {
        name = a;
        mail = b;
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

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}

package com.example.messangerapplication.Models;

public class Request {
    String mail,money,UID;

    public Request () {}

    public Request(String mail, String money, String UID) {
        this.mail = mail;
        this.money = money;
        this.UID = UID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}

package com.example.messangerapplication.Models;

public class Request {
    String mail,money,UID, reqUid;

    public Request () {}

    public Request(String mail, String money, String UID, String reqUid) {
        this.mail = mail;
        this.money = money;
        this.UID = UID;
        this.reqUid = reqUid;
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

    public String getReqUid() {
        return reqUid;
    }

    public void setReqUid(String reqUid) {
        this.reqUid = reqUid;
    }
}

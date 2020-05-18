package com.example.messangerapplication.Models;

public class PrivMess {
    private String NicknameLetter, NickName, ID, UID;

    public PrivMess () {}

    public PrivMess (String NicknameLetter, String NickName) {
        this.NickName = NickName;
        this.NicknameLetter = NicknameLetter;
    }

    public String getNicknameLetter() {
        return NicknameLetter;
    }

    public void setNicknameLetter(String nicknameLetter) {
        NicknameLetter = nicknameLetter;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}

package com.example.messangerapplication.Models;

public class PrivMess {
    String NicknameLetter, NickName, Message;
    Boolean You;

    public PrivMess () {}

    public PrivMess (String NicknameLetter, String NickName, String Message, Boolean You) {
        this.Message = Message;
        this.NickName = NickName;
        this.NicknameLetter = NicknameLetter;
        this.You = You;
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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getYou() {
        return You;
    }

    public void setYou(Boolean you) {
        You = you;
    }
}

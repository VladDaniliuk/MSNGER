package com.example.messangerapplication.Models;

public class Mess {
    String us,mes,uid;

    public  Mess(){}

    public Mess(String a, String b, String c){
        us = a;
        mes = b;
        uid = c;
    }

    public String getUs() {
        return us;
    }

    public void setUs(String us) {
        this.us = us;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

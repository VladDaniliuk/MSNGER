package com.example.messangerapplication.Models;

public class Mess {
    String mes,uid,us;
    boolean isR;

    public  Mess(){}

    public Mess(String a, String b, String c,boolean d){
        mes = a;
        uid = b;
        us = c;
        isR = d;
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

    public String getUs() {
        return us;
    }

    public void setUs(String us) {
        this.us = us;
    }

    public boolean getIsR() {
        return isR;
    }

    public void setIsR(boolean isR) {
        this.isR = isR;
    }
}

package com.example.messangerapplication.Models;

public class Mess {
    String mes,uid,us,mesuid,time,type;
    boolean isRead;

    public  Mess(){}

    public Mess(String a, String b, String c, String e, String d, String f, boolean g){
        mes = a;
        uid = b;
        us = c;
        mesuid = e;
        time = d;
        type = f;
        isRead = g;
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

    public String getMesuid() {
        return mesuid;
    }

    public void setMesuid(String mesuid) {
        this.mesuid = mesuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}

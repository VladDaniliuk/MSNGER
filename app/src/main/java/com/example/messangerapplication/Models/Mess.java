package com.example.messangerapplication.Models;

public class Mess {
    String mes,uid,us,mesuid,time;

    public  Mess(){}

    public Mess(String a, String b, String c, String e, String d){
        mes = a;
        uid = b;
        us = c;
        mesuid = e;
        time = d;
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
}

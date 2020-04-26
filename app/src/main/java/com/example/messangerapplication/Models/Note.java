package com.example.messangerapplication.Models;

public class Note {
    String not,uid,mesuid;

    public  Note(){}

    public Note(String a, String b, String c){
        not = a;
        uid = b;
        mesuid = c;
    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMesuid() {
        return mesuid;
    }

    public void setMesuid(String mesuid) {
        this.mesuid = mesuid;
    }
}
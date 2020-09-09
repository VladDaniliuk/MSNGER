package com.example.messangerapplication.Models;

import java.util.ArrayList;

public class Note {
    String not,uid,mesuid;
    ArrayList<String> hash = new ArrayList<>();

    public  Note(){}

    public Note(String a, String b, String c, ArrayList<String> d){
        not = a;
        uid = b;
        mesuid = c;
        hash = d;
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

    public ArrayList<String> getHash() {
        return hash;
    }

    public void setHash(ArrayList<String> hash) {

        this.hash.addAll(hash);
    }
}
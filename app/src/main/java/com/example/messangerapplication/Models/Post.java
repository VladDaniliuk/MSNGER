package com.example.messangerapplication.Models;

public class Post {
    String name, text, time, id, uid;

    public Post(){}

    public Post(String name, String text, String time, String id, String uid) {
        this.name = name;
        this.text = text;
        this.time = time;
        this.id = id;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

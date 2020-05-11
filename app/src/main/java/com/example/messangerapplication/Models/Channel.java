package com.example.messangerapplication.Models;

public class Channel {
    String ID, name;

    public Channel() {}

    public Channel(String ID, String name) {
        this.name = name;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.dartero.database;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    private String name;

    public User (String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

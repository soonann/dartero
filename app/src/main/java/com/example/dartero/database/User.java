package com.example.dartero.database;

import com.google.gson.annotations.SerializedName;

/**
 * User model
 */
public class User {
    @SerializedName("username")
    private String username;

    public User (String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String toString() { return "Player name: " + username; }
}

package com.example.dartero.database;

import com.google.gson.annotations.SerializedName;

public class Score implements Comparable<Score> {
    @SerializedName("username")
    private String userName;
    @SerializedName("score")
    private int score;

    public Score(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Score s) {
        if (this.score > s.getScore())
            return -1;
        else if (this.score < s.getScore())
            return 1;
        else return 0;
    }

    public String toString() {
        return "Username : " + userName + " Score : " + score + "\n";
    }
}

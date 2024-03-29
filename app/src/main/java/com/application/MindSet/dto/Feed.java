package com.application.MindSet.dto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class Feed {

    private String local;
    private String Username;
    private String numberOfPlayer;
    private String date;
    private String gameID;
    private String sport;

    public Feed(String gameID, String username, String local, String date, String numberOfPlayer, String sport) {
        this.local = local;
        Username = username;
        this.numberOfPlayer = numberOfPlayer;
        this.date = date;
        this.gameID = gameID;
        this.sport = sport;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(String numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGameID() {
        return gameID;
    }

    public String getSport() {
        return sport;
    }
}
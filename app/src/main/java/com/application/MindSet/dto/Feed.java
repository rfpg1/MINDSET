package com.application.MindSet.dto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class Feed {

    private String local;
    private String Username;
    private String numberOfPlayer;
    private Date date;

    public Feed(String username, String local, Date date, String numberOfPlayer) {
        this.local = local;
        Username = username;
        this.numberOfPlayer = numberOfPlayer;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
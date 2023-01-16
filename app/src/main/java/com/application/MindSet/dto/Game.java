package com.application.MindSet.dto;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Game implements Serializable {

    private String ownerID;
    private String sport;
    private Date date;
    private LatLng local;
    private List<String> participantsID;

    public Game(String ownerID, String sport, Date date, LatLng local, List<String> participantsID){
        this.ownerID = ownerID;
        this.sport = sport;
        this.date = date;
        this.local = local;
        this.participantsID = participantsID;
    }

    public void addParticipant(String id){
        if(!participantsID.contains(id)){
            participantsID.add(id);
        }
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getSport() {
        return sport;
    }

    public Date getDate() {
        return date;
    }

    public LatLng getLocal() {
        return local;
    }

    public List<String> getParticipantsID() {
        return participantsID;
    }

    public boolean removeParticipant(String id){
        return participantsID.remove(id);
    }
}

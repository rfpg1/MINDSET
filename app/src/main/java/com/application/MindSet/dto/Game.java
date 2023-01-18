package com.application.MindSet.dto;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Game implements Serializable {

    private String ownerID;
    private String sport;
    private Date date;
    private double latitude;
    private double longitude;
    private List<String> participantsID;
    private String localName;

    public Game() {
    }

    public Game(String ownerID, String sport, Date date, LatLng local, List<String> participantsID, String localName){
        this.ownerID = ownerID;
        this.sport = sport;
        this.date = date;
        this.latitude = local.latitude;
        this.longitude = local.longitude;
        this.participantsID = participantsID;
        this.localName = localName;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getParticipantsID() {
        return participantsID;
    }

    public boolean removeParticipant(String id){
        return participantsID.remove(id);
    }

    public String getLocalName() {
        return localName;
    }

    @Override
    public String toString() {
        return "Game{" +
                "ownerID='" + ownerID + '\'' +
                ", sport='" + sport + '\'' +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", participantsID=" + participantsID +
                '}';
    }
}

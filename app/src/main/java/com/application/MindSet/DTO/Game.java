package com.application.MindSet.DTO;

import java.sql.Date;
import java.util.ArrayList;

public class Game {

    private String ownerID;
    private String sport;
    private Date date;
    private String local;
    private ArrayList<String> participantsID;

    public Game(String ownerID, String sport, Date date, String local, ArrayList<String> participantsID){
        this.ownerID = ownerID;
        this.sport = sport;
        this.date = date;
        this.local = local;
        this.participantsID = (ArrayList<String>) participantsID.clone();
    }

    public void addParticipant(String id){
        if(!participantsID.contains(id)){
            participantsID.add(id);
        }
    }

    public boolean removeParticipant(String id){
        return participantsID.remove(id);
    }
}

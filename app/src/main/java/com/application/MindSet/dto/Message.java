package com.application.MindSet.dto;

import java.util.Date;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private Date date;
    private boolean invited;
    private String gameId;
    private String sport;

    public Message() {
    }

    public Message(String sender, String receiver, String message,
                   boolean invited, String gameId, String sport) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = new Date();
        this.invited = invited;
        this.gameId = gameId;
        this.sport = sport;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public String getGameId() {
        return gameId;
    }

    public String getSport() {
        return sport;
    }

    public boolean getInvited() {
        return invited;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}

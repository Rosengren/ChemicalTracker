package com.chemicaltracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequest {

    private String requestType; // ADD or REMOVE
    private String username;
    private String location;
    private String room;
    private String cabinet;
    private String chemical;

    public UpdateRequest() {
        requestType = "";
        username = "";
        location = "";
        room = "";
        cabinet = "";
        chemical = "";
    }

    public UpdateRequest(final String requestType, final String username, final String location,
            final String room, final String cabinet, final String chemical) {

        this.requestType = requestType;
        this.username = username;
        this.location = location;
        this.room = room;
        this.cabinet = cabinet;
        this.chemical = chemical;
    }

    public String toString() {
        return "{ 'requestType' : " + this.requestType +
                ", 'username' : " + this.username +
                ", 'location' : " + this.location +
                ", 'room' : " + this.room +
                ", 'cabinet' : " + this.cabinet +
                ", 'chemical' : " + this.chemical +
                "}";
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCabinet() {
        return this.cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public String getChemical() {
        return this.chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }
}

package com.chemicaltracker.controller.api.request;

public class UpdateRequest {

    private String requestType; // ADD or REMOVE
    private String location;
    private String room;
    private String cabinet;
    private String chemical;

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


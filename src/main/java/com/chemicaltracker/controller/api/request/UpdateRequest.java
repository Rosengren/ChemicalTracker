package com.chemicaltracker.controller.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequest {

    private String request; // ADD or REMOVE or FORK
    private String location;
    private String room;
    private String cabinet;
    private String chemical;
    private String auditVersion;
    private String forkVersion;
    private boolean forkWithChemicals;

    public boolean isForkWithChemicals() {
        return forkWithChemicals;
    }

    public void setForkWithChemicals(boolean forkWithChemicals) {
        this.forkWithChemicals = forkWithChemicals;
    }


    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
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

    public void setRoom(String room) { this.room = room; }

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

    public String getAuditVersion() {
        return auditVersion;
    }

    public void setAuditVersion(String auditVersion) {
        this.auditVersion = auditVersion;
    }

    public String getForkVersion() {
        return forkVersion;
    }

    public void setForkVersion(String forkVersion) {
        this.forkVersion = forkVersion;
    }
}


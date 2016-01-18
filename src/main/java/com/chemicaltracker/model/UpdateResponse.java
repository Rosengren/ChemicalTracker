package com.chemicaltracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResponse {

    private Boolean success;
    private UpdateStatus status;
    private String message;

    public UpdateResponse(Boolean success, UpdateStatus status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }

    public UpdateResponse() {
        this(false, UpdateStatus.UNKNOWN_ERROR, "");
    }

    public String toJSONString() {
        return "{ \"success\" : " + success.toString() +
                ", \"status\" : \"" + status.toString() + "\"" +
                ", \"message\" : \"" + message + "\"" +
                "}";
    }

    public void setSuccess(Boolean success) { this.success = success; }
    public Boolean getSuccess() { return this.success; }
    public void setStatus(UpdateStatus status) { this.status = status; }
    public UpdateStatus getStatus() { return this.status; }
    public void setMessage(String msg) { this.message = msg; }
    public String getMessage() { return this.message; }
}

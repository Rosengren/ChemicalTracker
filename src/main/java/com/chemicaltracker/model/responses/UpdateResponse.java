package com.chemicaltracker.model.responses;

import com.chemicaltracker.model.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResponse {

    private Boolean success;
    private UpdateStatus status;
    private String message;
    private String imageURL;

    public UpdateResponse(UpdateStatus status) {
        this.status = status;
        this.success = status.getSuccess();
        this.message = status.getMessage();
        this.imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
    }

    public UpdateResponse() {
        this(UpdateStatus.UNKNOWN_ERROR);
    }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
    public String getImageURL() { return this.imageURL; }
    public void setSuccess(Boolean success) { this.success = success; }
    public Boolean getSuccess() { return this.success; }
    public void setStatus(UpdateStatus status) { this.status = status; }
    public UpdateStatus getStatus() { return this.status; }
    public void setMessage(String msg) { this.message = msg; }
    public String getMessage() { return this.message; }
}

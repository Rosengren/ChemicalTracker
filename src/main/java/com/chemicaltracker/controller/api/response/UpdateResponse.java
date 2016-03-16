package com.chemicaltracker.controller.api.response;

public class UpdateResponse {

    private static final String PLACEHOLDER_IMAGE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";

    private Boolean success;
    private UpdateStatus status;
    private String message;
    private String imageURL;

    public UpdateResponse(UpdateStatus status) {
        this.status = status;
        this.success = status.getSuccess();
        this.message = status.getMessage();
        this.imageURL = PLACEHOLDER_IMAGE_URL;
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

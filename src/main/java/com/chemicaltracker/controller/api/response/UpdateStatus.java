package com.chemicaltracker.controller.api.response;

public enum UpdateStatus {
    INVALID_REQUEST_TYPE(false, "The request type is invalid"),
    INVALID_CHEMICAL(false, "The chemical does not exist in the database"),
    MISSING_STORAGE_FIELD(false, "One or more storage fields are missing!"),
    ADDED_LOCATION(true, "Successfully added location!"),
    REMOVED_LOCATION(true, "Successfully removed location!"),
    ADDED_ROOM(true, "Successfully added room!"),
    REMOVED_ROOM(true, "Successfully removed room!"),
    ADDED_CABINET(true, "Successfully added cabinet!"),
    REMOVED_CABINET(true, "Successfully removed cabinet!"),
    ADDED_CHEMICAL(true, "Successfully added chemical!"),
    REMOVED_CHEMICAL(true, "Successfully removed chemical!"),
    UNKNOWN_ERROR(false, "Unknown error occurred!");

    private final Boolean success;
    private final String message;

    UpdateStatus(final Boolean success, final String msg) {
        this.success = success;
        this.message = msg;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }
}

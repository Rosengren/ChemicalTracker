package com.chemicaltracker.controller.api.response;

public enum UpdateStatus {
    INVALID_REQUEST_TYPE(false, "The request type is invalid"),
    INVALID_CHEMICAL(false, "The chemical does not exist in the database"),
    INVALID_STORAGE(false, "One or more storage fields do not exist"),
    STORAGE_ALREADY_EXISTS(false, "The storage location already exists"),
    MISSING_STORAGE_FIELD(false, "One or more storage fields are missing!"),
    ADDED_LOCATION(true, "Successfully added location!"),
    REMOVED_LOCATION(true, "Successfully removed location!"),
    ADDED_ROOM(true, "Successfully added room!"),
    REMOVED_ROOM(true, "Successfully removed room!"),
    ADDED_CABINET(true, "Successfully added cabinet!"),
    REMOVED_CABINET(true, "Successfully removed cabinet!"),
    ADDED_CHEMICAL(true, "Successfully added chemical!"),
    REMOVED_CHEMICAL(true, "Successfully removed chemical!"),
    MISSING_AUDIT_VERSION(false, "The audit version is missing!"),
    MISSING_FORK_VERSION(false, "The fork version is missing!"),
    FORK_VERSION_ALREADY_EXISTS(false, "The fork version name already exists!"),
    FORKED_CABINET(true, "Successfully forked cabinet!"),
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

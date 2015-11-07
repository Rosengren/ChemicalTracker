package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * This class is used to represent a general storage object for chemicals 
 * Example: room, cabinet, location, etc.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Storage {

    // TODO: maybe add a field to determine if this is a new storage an existing storage
    private String username;
    private String name;
    private String id;
    private String description;
    private Map<String, String> storedItemIDs;

    public Storage() {
        storedItemIDs = new HashMap<String, String>();
    }

    public Storage(final String username, final String name, final String id, final String description,
            final Map<String, String> storedItemIDs) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.description = description;
        this.storedItemIDs = storedItemIDs;
    }

    public void setID(final String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void setStoredItemIDs(final Map<String, String> storedItemIDs) {
        this.storedItemIDs = storedItemIDs;
    }

    public void addStoredItemID(final String storedItemID, final String storedItemName) {
        this.storedItemIDs.put(storedItemID, storedItemName);
    }

    public Map<String, String> getStoredItemIDs() {
        return this.storedItemIDs;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String toJSON() {
        JSONObject json = new JSONObject();

        json.put("name", this.name);
        json.put("description", this.description);
        json.put("storedItemIDs", this.storedItemIDs);

        return json.toString();
    }
}


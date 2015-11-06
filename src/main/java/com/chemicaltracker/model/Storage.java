package com.chemicaltracker.model;

import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;

/*
 * This class is used to represent a general storage object for chemicals 
 * Example: room, cabinet, location, etc.
 */
public class Storage {

    // TODO: maybe add a field to determine if this is a new storage an existing storage
    private String username;
    private String name;
    private String description;
    private List<String> storedItemNames;

    public Storage() {
        storedItemNames = new ArrayList<String>();
    }

    public Storage(final String username, final String name, final String description,
            final List<String> storedItemNames) {
        this.username = username;
        this.name = name;
        this.description = description;
        this.storedItemNames = storedItemNames;
    }

    public void setStoredItemNames(final List<String> storedItemNames) {
        this.storedItemNames = storedItemNames;
    }

    public void addStoredItemName(final String storedItemName) {
        this.storedItemNames.add(storedItemName);
    }

    public List<String> getStoredItemNames() {
        return this.storedItemNames;
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
        json.put("storedItemNames", this.storedItemNames);

        return json.toString();
    }
}


package com.chemicaltracker.model;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.Map.Entry;

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

    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String IMAGE_URL = "Image URL";

    private String username;
    private String name;
    private String id;
    private String description;
    private String imageURL;
    private Map<String, String> storedItemMap;
    private List<String> storedItemIDs;
    private List<String> storedItemNames;

    public Storage() {
        storedItemMap = new HashMap<String, String>();
        storedItemIDs = new ArrayList<String>();
        storedItemNames = new ArrayList<String>();
        imageURL = "placeholder.jpg";
    }

    public Storage(final String username, final String name, final String id, final String description,
            final Map<String, String> storedItemMap, final String imageURL) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.description = description;
        this.storedItemMap = storedItemMap;
        setNamesAndIDs();

        this.imageURL = imageURL;
    }

    public Storage(final String username, final String name, final String id,
            final String description, final String imageURL) {
        this(username, name, id, description, new HashMap<String, String>(), imageURL);
    }

    public Storage(final String username, final String name, final String id, final String description,
            final Map<String, String> storedItemMap) {
        this(username, name, id, description, storedItemMap, "placeholder.jpg");
    }

    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setID(final String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void setStoredItemMap(final Map<String, String> storedItemMap) {
        this.storedItemMap = storedItemMap;
        setNamesAndIDs();
    }

    public void addStoredItem(final String storedItemID, final String storedItemName) {
        this.storedItemMap.put(storedItemID, storedItemName);
        this.storedItemIDs.add(storedItemID);
        this.storedItemNames.add(storedItemName);
    }

    public Set<Entry<String, String>> getStoredItemsSet() {
        return this.storedItemMap.entrySet();
    }

    public String getStoredItemID(final String name) {
        return this.storedItemMap.get(name);
    }

    public List<String> getStoredItemIDs() {
        return this.storedItemIDs;
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

    private void setNamesAndIDs() {
        this.storedItemNames = new ArrayList<String>(this.storedItemMap.keySet());

        this.storedItemIDs = new ArrayList<String>();
        for (String Name : this.storedItemNames) {
            this.storedItemIDs.add(this.storedItemMap.get(Name));
        }
    }
}


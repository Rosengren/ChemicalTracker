package com.chemicaltracker.persistence.model;

import java.util.*;
import java.util.Map.Entry;

// Annotations
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Locations")
public class Location extends AbstractStorageComponent implements StorageComponent {

    private String id;
    private String username;
    private String description;
    private String imageURL;
    private Map<String, String> roomNames;

    public Location() {
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        roomNames = new HashMap<>();
    }

    @DynamoDBHashKey(attributeName="Username")
    public String getUsername() { return this.username; }
    public void setUsername(final String username) { this.username = username; }
    public Location withUsername(final String username) { setUsername(username); return this; }

    @DynamoDBRangeKey(attributeName="Location ID")
    public String getID() { return this.id; }
    public void setID(final String id) { this.id = id; }
    public Location withID(final String id) { setID(id); return this; }

    @DynamoDBAttribute(attributeName="Image URL")
    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public Location withImageURL(final String imageURL) { setImageURL(imageURL); return this; }

    @DynamoDBAttribute(attributeName="Name")
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }
    public Location withName(final String name) { setName(name); return this; }

    @DynamoDBAttribute(attributeName="Description")
    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }
    public Location withDescription(final String desc) { setDescription(desc); return this; }

    @DynamoDBAttribute(attributeName="Room Names")
    public Map<String, String> getRoomNames() { return this.roomNames; }
    public void setRoomNames(final Map<String, String> roomNames) { this.roomNames = roomNames; }

    @DynamoDBIgnore
    public void addStoredItem(final String storedItemID, final String storedItemName) {
        this.roomNames.put(storedItemID, storedItemName);
    }

    @DynamoDBIgnore
    public void removeStoredItem(final String storedItemID) {
        this.roomNames.remove(storedItemID);
    }

    @DynamoDBIgnore
    public Set<Entry<String, String>> getStoredItemsSet() {
        return this.roomNames.entrySet();
    }

    @DynamoDBIgnore
    public String getStoredItemID(final String name) {
        return roomNames.get(name);
    }

    @DynamoDBIgnore
    public List<String> getStoredItemIDs() {
        return new ArrayList<String>(roomNames.values());
    }

    @DynamoDBIgnore
    public List<String> getStoredItemNames() {
        return new ArrayList<String>(roomNames.keySet());
    }
}
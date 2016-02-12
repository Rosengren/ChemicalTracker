package com.chemicaltracker.persistence.model;

import java.util.*;
import java.util.Map.*;
import com.chemicaltracker.model.StorageTag;

// Annotations
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Cabinets")
public class Cabinet extends AbstractStorageComponent implements StorageComponent {

    private String id;
    private String name;
    private String username;
    private String description;
    private String imageURL;
    private Set<StorageTag> tags;
    private Map<String, String> roomNames;

    public Cabinet() {
        super();
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        tags = new HashSet<>();
        tags.add(StorageTag.IGNORE); // can't be blank
    }

    @DynamoDBHashKey(attributeName="Username")
    public String getUsername() { return this.username; }
    public void setUsername(final String username) { this.username = username; }
    public Cabinet withUsername(final String username) { setUsername(username); return this; }

    @DynamoDBRangeKey(attributeName="Cabinet ID")
    public String getID() { return this.id; }
    public void setID(final String id) { this.id = id; }
    public Cabinet withID(final String id) { setID(id); return this; }

    @DynamoDBAttribute(attributeName="Image URL")
    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public Cabinet withImageURL(final String imageURL) { setImageURL(imageURL); return this; }

    @DynamoDBAttribute(attributeName="Name")
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }
    public Cabinet withName(final String name) { setName(name); return this; }

    @DynamoDBAttribute(attributeName="Description")
    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }
    public Cabinet withDescription(final String desc) { setDescription(desc); return this; }

    @DynamoDBAttribute(attributeName="Chemical Names")
    public Map<String, String> getRoomNames() { return this.roomNames; }
    public void setRoomNames(final Map<String, String> roomNames) { this.roomNames = roomNames; }

    @DynamoDBAttribute(attributeName="Tags") // TODO: replace setTags
    public Set<String> getTagNames() {
        Set<String> tagNames = new HashSet<>();
        for (StorageTag tag : tags) {
            tagNames.add(tag.name());
        }
        return tagNames;
    }

    @DynamoDBIgnore
    public void addChemical(final String chemicalID, final String chemicalName) {
        this.roomNames.put(chemicalID, chemicalName);
    }

    @DynamoDBIgnore
    public void removeChemical(final String chemicalID) {
        this.roomNames.remove(chemicalID);
    }

    @DynamoDBIgnore
    public void addTag(final StorageTag tag) {
        tags.add(tag);
    }

    @DynamoDBIgnore
    public void addTag(final String tag) {
        if (StorageTag.contains(tag)) {
            this.tags.add(StorageTag.valueOf(tag));
        }
    }

    @DynamoDBIgnore
    public void addTags(final List<String> tags) {
        for (String tag : tags) {
            addTag(tag);
        }
    }

    public void setTags(final Set<String> tags) { // TODO: simplify
        for (String tagName : tags) {
            addTag(tagName);
        }
    }

    @DynamoDBIgnore
    public List<StorageTag> getTags() {
        List<StorageTag> tagList = new ArrayList<>();
        tagList.addAll(tags);
        return tagList;
    }

    public void setTagNames(final Set<String> tags) {
        setTags(tags);
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
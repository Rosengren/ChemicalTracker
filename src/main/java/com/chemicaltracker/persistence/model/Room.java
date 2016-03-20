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

@DynamoDBTable(tableName="Rooms")
public class Room extends AbstractStorageComponent implements StorageComponent {

    private String id;
    private String username;
    private String description;
    private String imageURL;
    private Set<StorageTag> tags;
    private Map<String, String> cabinetNames; // REMOVE UNUSED VARIABLES

    public Room() {
        super();
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        tags = new HashSet<>();
        cabinetNames = new HashMap<>();
        tags.add(StorageTag.IGNORE); // can't be empty
    }

    @DynamoDBHashKey(attributeName="Username")
    public String getUsername() { return this.username; }
    public void setUsername(final String username) { this.username = username; }
    public Room withUsername(final String username) { setUsername(username); return this; }

    @DynamoDBRangeKey(attributeName="Room ID")
    public String getID() { return this.id; }
    public void setID(final String id) { this.id = id; }
    public Room withID(final String id) { setID(id); return this; }

    @DynamoDBAttribute(attributeName="Image URL")
    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public Room withImageURL(final String imageURL) { setImageURL(imageURL); return this; }

    @DynamoDBAttribute(attributeName="Name")
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }
    public Room withName(final String name) { setName(name); return this; }

    @DynamoDBAttribute(attributeName="Description")
    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }
    public Room withDescription(final String desc) { setDescription(desc); return this; }

    @DynamoDBAttribute(attributeName="Cabinet Names")
    public Map<String, String> getCabinetNames() { return this.cabinetNames; }
    public void setCabinetNames(final Map<String, String> cabinetNames) { this.cabinetNames = cabinetNames; }

    @DynamoDBIgnore
    public void addStoredItem(final String storedItemID, final String storedItemName) {
        this.cabinetNames.put(storedItemID, storedItemName);
    }

    @DynamoDBIgnore
    public void removeStoredItem(final String storedItemID) {
        this.cabinetNames.remove(storedItemID);
    }

    @DynamoDBIgnore
    public void addElement(final StorageComponent element) {
        elements.add(element);
    }

    @DynamoDBIgnore
    public List<StorageComponent> getElements() {
        return elements;
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
    public void resetTags(final String tag) {
        tags = new HashSet<StorageTag>();
    }

    @DynamoDBIgnore
    public List<StorageTag> getTags() {
        List<StorageTag> tagList = new ArrayList<StorageTag>();
        tagList.addAll(tags);
        return tagList;
    }

    @DynamoDBAttribute(attributeName="Tags") // TODO: replace setTags
    public Set<String> getTagNames() {
        Set<String> tagNames = new HashSet<String>();
        for (StorageTag tag : tags) {
            tagNames.add(tag.name());
        }
        return tagNames;
    }

    public void setTagNames(final Set<String> tags) {
        setTags(tags);
    }

    @DynamoDBIgnore
    public Set<Entry<String, String>> getStoredItemsSet() {
        return this.cabinetNames.entrySet();
    }

    @DynamoDBIgnore
    public String getStoredItemID(final String name) {
        return cabinetNames.get(name);
    }

    @DynamoDBIgnore
    public List<String> getStoredItemIDs() {
        return new ArrayList<String>(cabinetNames.values());
    }

    @DynamoDBIgnore
    public List<String> getStoredItemNames() {
        return new ArrayList<String>(cabinetNames.keySet());
    }
}
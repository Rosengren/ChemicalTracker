package com.chemicaltracker.model;

import com.chemicaltracker.model.StorageTag;

import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;

import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/*
 * This class is used to represent a general storage object for chemicals 
 * Example: room, cabinet, location, etc.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName="Locations")
public class Location extends AbstractStorageComponent implements StorageComponent {

    private List<StorageComponent> elements;

    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String TAGS = "Tags";
    public static final String IMAGE_URL = "Image URL";

    private String username;
    private String id;
    private String description;
    private String imageURL;
    private Set<StorageTag> tags;
    private Map<String, String> roomNames; // REMOVE UNUSED VARIABLES

    public Location() {
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        elements = new ArrayList<StorageComponent>();
        tags = new HashSet<StorageTag>();
    }

    @DynamoDBIgnore
    public void addStoredItem(final String storedItemID, final String storedItemName) {
        this.roomNames.put(storedItemID, storedItemName);
    }

    @DynamoDBIgnore
    public void removeStoredItem(final String storedItemID) {
        this.roomNames.remove(storedItemID);
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

    // @DynamoDBIgnore
    // public void setTags(final Set<StorageTag> tags) {
    //     this.tags = tags; 
    // }

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

    @Override
    public Phrase getFormattedPDF(final int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);

        addBody(content, level);

        return content;
    }

    private void addBody(Paragraph content, int level) {

        com.itextpdf.text.List body = new com.itextpdf.text.List();
        body.setListSymbol("");

        if (elements.isEmpty()) {
            body.setIndentationLeft((level + 1) * INDENT_WIDTH);
            body.add(new ListItem("No further elements"));
        } else {
            for (StorageComponent element : elements) {
                body.add(new ListItem(element.getFormattedPDF(level + 1)));
                createEmptyLine(body, 1);
            }
        }

        content.add(body);
    }


    // Getters and Setters
    
    @DynamoDBAttribute(attributeName="Image URL")
    public String getImageURL() { return this.imageURL; }    
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }

    @DynamoDBRangeKey(attributeName="Location ID")
    public String getID() { return this.id; }
    public void setID(final String id) { this.id = id; }

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
    
    @DynamoDBHashKey(attributeName="Username")
    public String getUsername() { return this.username; }
    public void setUsername(final String username) { this.username = username; }

    @DynamoDBAttribute(attributeName="Name")
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }

    @Override
    @DynamoDBAttribute(attributeName="Description")
    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }

    @DynamoDBAttribute(attributeName="Room Names")
    public Map<String, String> getRoomNames() { return this.roomNames; }
    public void setRoomNames(final Map<String, String> roomNames) { this.roomNames = roomNames; }
}
package com.chemicaltracker.model.storage;

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

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName="Locations")
public class Location extends AbstractStorageComponent implements StorageComponent {

    private List<StorageComponent> elements;

    private String username;
    private String id;
    private String description;
    private String imageURL;
    private Map<String, String> roomNames;

    public Location() {
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        elements = new ArrayList<StorageComponent>();
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

    @Override
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
    public void addElement(final StorageComponent element) {
        elements.add(element);
    }

    @DynamoDBIgnore
    public List<StorageComponent> getElements() {
        return elements;
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
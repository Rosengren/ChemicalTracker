package com.chemicaltracker.model;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

//import java.util.List;
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
public class Storage extends AbstractStorageComponent implements StorageComponent {

    private java.util.List<StorageComponent> elements;

    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String IMAGE_URL = "Image URL";

    private String username;
    private String id;
    private String description;
    private String imageURL;
    private Map<String, String> storedItemMap;
    private java.util.List<String> storedItemIDs;
    private java.util.List<String> storedItemNames;

    public Storage() {
        storedItemMap = new HashMap<String, String>();
        storedItemIDs = new ArrayList<String>();
        storedItemNames = new ArrayList<String>();
        imageURL = "placeholder.jpg";
        elements = new ArrayList<StorageComponent>();
    }

    public Storage(final String username, final String name, final String id, final String description,
            final Map<String, String> storedItemMap, final String imageURL) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.description = description;
        this.storedItemMap = storedItemMap;
        setNamesAndIDs();

        elements = new ArrayList<StorageComponent>();
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

    public void setStoredItemMap(final Map<String, String> storedItemMap) {
        this.storedItemMap = storedItemMap;
        setNamesAndIDs();
    }

    public void addStoredItem(final String storedItemID, final String storedItemName) {
        this.storedItemMap.put(storedItemID, storedItemName);
        this.storedItemIDs.add(storedItemID);
        this.storedItemNames.add(storedItemName);
    }

    public void addElement(StorageComponent element) {
        elements.add(element);
    }

    @Override
    public Phrase getFormattedPDF(int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);

        addBody(content, level);

        return content;
    }

    private void addBody(Paragraph content, int level) {

        List body = new List();
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
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public String getImageURL() { return this.imageURL; }
    public void setID(final String id) { this.id = id; }
    public String getID() { return this.id; }
    public Set<Entry<String, String>> getStoredItemsSet() { return this.storedItemMap.entrySet(); }
    public String getStoredItemID(final String name) { return this.storedItemMap.get(name); }
    public java.util.List<String> getStoredItemIDs() { return this.storedItemIDs; }
    public java.util.List<String> getStoredItemNames() { return this.storedItemNames; }
    public void setUsername(final String username) { this.username = username; }
    public String getUsername() { return this.username; }
    public void setName(final String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setDescription(final String description) { this.description = description; }

    @Override
    public String getDescription() { return this.description; }

    private void setNamesAndIDs() {
        this.storedItemNames = new ArrayList<String>(this.storedItemMap.keySet());

        this.storedItemIDs = new ArrayList<String>();
        for (String Name : this.storedItemNames) {
            this.storedItemIDs.add(this.storedItemMap.get(Name));
        }
    }
}


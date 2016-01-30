package com.chemicaltracker.model;

import com.chemicaltracker.model.StorageTag;

// import com.itextpdf.text.List;
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

/*
 * This class is used to represent a general storage object for chemicals 
 * Example: room, cabinet, location, etc.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Storage extends AbstractStorageComponent implements StorageComponent {

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
    private Map<String, String> storedItemMap;
    private List<String> storedItemIDs;
    private List<String> storedItemNames;

    public Storage() {
        storedItemMap = new HashMap<String, String>();
        storedItemIDs = new ArrayList<String>();
        storedItemNames = new ArrayList<String>();
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        elements = new ArrayList<StorageComponent>();
        tags = new HashSet<StorageTag>();
    }

    public Storage(final String username, final String name, final String id, final String description,
            final List<String> tags, final Map<String, String> storedItemMap, final String imageURL) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.description = description;
        this.storedItemMap = storedItemMap;
        this.tags = new HashSet<StorageTag>();
        addTags(tags);
        setNamesAndIDs();

        elements = new ArrayList<StorageComponent>();
        this.imageURL = imageURL;
    }

    public Storage(final String username, final String name, final String id,
            final String description, final String imageURL) {
        this(username, name, id, description, new ArrayList<String>(), new HashMap<String, String>(), imageURL);
    }

    public Storage(final String username, final String name, final String id, final String description,
            final Map<String, String> storedItemMap) {
        this(username, name, id, description, new ArrayList<String>(), storedItemMap, "placeholder.jpg");
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

    public void removeStoredItem(final String storedItemID) {
        String itemName = this.storedItemMap.get(storedItemID);
        this.storedItemMap.remove(storedItemID);
        this.storedItemIDs.remove(itemName);
        this.storedItemNames.remove(storedItemID);
    }

    public void addElement(final StorageComponent element) {
        elements.add(element);
    }

    public List<StorageComponent> getElements() {
        return elements;
    }

    public void addTag(final StorageTag tag) {
        tags.add(tag);
    }

    public void addTag(final String tag) {

        if (tag.equals("FLAMMABLE")) {
            tags.add(StorageTag.FLAMMABLE);
        } else if (tag.equals("UNSTABLE")) {
            tags.add(StorageTag.UNSTABLE);
        } else if (tag.equals("HEALTH")) {
            tags.add(StorageTag.HEALTH);
        } else if (tag.equals("INCOMPATIBLE")) {
            tags.add(StorageTag.INCOMPATIBLE);
        } else if (tag.equals("OXIDIZING_AGENTS")) {
            tags.add(StorageTag.OXIDIZING_AGENTS);    
        } else if (tag.equals("REDUCTION_AGENTS")) {
            tags.add(StorageTag.REDUCTION_AGENTS);
        }
    }

    public void addTags(final List<String> tags) {
        for (String tag : tags) {
            addTag(tag);
        }
    }

    public void setTags(final Set<StorageTag> tags) {
        this.tags = tags; 
    }

    public void resetTags(final String tag) {
        tags = new HashSet<StorageTag>();
    }

    public List<StorageTag> getTags() {
        List<StorageTag> tagList = new ArrayList<StorageTag>();
        tagList.addAll(tags);
        return tagList;
    }

    public List<String> getTagNames() {
        List<String> tagNames = new ArrayList<String>();
        for (StorageTag tag : tags) {
            tagNames.add(tag.getTitle());
        }
        return tagNames;
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
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public String getImageURL() { return this.imageURL; }
    public void setID(final String id) { this.id = id; }
    public String getID() { return this.id; }
    public Set<Entry<String, String>> getStoredItemsSet() { return this.storedItemMap.entrySet(); }
    public String getStoredItemID(final String name) { return this.storedItemMap.get(name); }
    public List<String> getStoredItemIDs() { return this.storedItemIDs; }
    public List<String> getStoredItemNames() { return this.storedItemNames; }
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


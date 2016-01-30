package com.chemicaltracker.model;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import java.util.Map;
import java.util.HashMap;

// Composite Design Pattern: Leaf
public class Chemical extends AbstractStorageComponent implements StorageComponent {

    // DB field names
    public static final String NAME = "Name";
    public static final String IMAGE_URL = "Image URL";

    private String imageURL;
    private FireDiamond fireDiamond;
    private ChemicalProperties properties;
    private Boolean match;

    public Chemical() {
        this.properties = new ChemicalProperties();
        this.fireDiamond = new FireDiamond();
        this.name = "";
        this.match = false;
        this.imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
    }

    public Chemical(String name) {
        this();
        this.name = name;
        this.match = true;
    }

    public Chemical(String name, FireDiamond fireDiamond) {
        this(name);
        this.match = true;
        this.fireDiamond = fireDiamond;
    }

    public Chemical(String name, FireDiamond fireDiamond, Map<String, Map<String, String>> properties) {
        this.name = name;
        this.match = true;
        this.fireDiamond = fireDiamond;
        this.properties.setProperties(properties);
    }

    public String toJSONString() {
        return "{ \"match\" :  \"" + this.match.toString() + "\"" +
                ", \"chemical\" : \"" + this.name + "\"" +
                ", \"properties\" : " + this.fireDiamond.toJSONString() + "}";
    }

    @Override
    public Phrase getFormattedPDF(final int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);
        return content;
    }

    @Override
    public String getDescription() {
        String desc = "";
        desc += "Flammability: " + fireDiamond.getFlammability()
            + ", Health: " + fireDiamond.getHealth()
            + ", Instability: " + fireDiamond.getInstability()
            + ", Notice: " + fireDiamond.getNotice();

        Map<String, String> handlingAndStorage = properties.getProperty("Handling and Storage");

        desc += "\n\nStorage:\n" + handlingAndStorage.get("Storage");
        return desc;
    }

    public void addProperty(String name, Map<String, String> property) {
        this.properties.addProperty(name, property);
    }

    // Setters and Getters
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public String getImageURL() { return this.imageURL; }
    public void setProperties(Map<String, Map<String, String>> properties) { this.properties.setProperties(properties); }
    public Map<String, Map<String, String>> getProperties() { return this.properties.getProperties(); }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setFireDiamond(FireDiamond fireDiamond) { this.fireDiamond = fireDiamond; }
    public FireDiamond getFireDiamond() { return fireDiamond; }
    public void setMatch(final Boolean match) { this.match = match; }
    public Boolean getMatch() { return match; }
}

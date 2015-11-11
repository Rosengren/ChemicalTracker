package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

public class Chemical {

    private String name;
    private String imageURL;
    private FireDiamond fireDiamond;
    private ChemicalProperties properties;

    public Chemical() {
        properties = new ChemicalProperties();
        // initialize with blank values
        fireDiamond = new FireDiamond();
        name = "";
        imageURL = "placeholder.jpg";
    }

    public Chemical(String name) {
        this.name = name;
        properties = new ChemicalProperties();
    }

    public Chemical(String name, FireDiamond fireDiamond) {
        this(name);
        this.fireDiamond = fireDiamond;
    }

    public Chemical(String name, FireDiamond fireDiamond, Map<String, Map<String, String>> properties) {
        this.name = name;
        this.fireDiamond = fireDiamond;
        this.properties.setProperties(properties);
    }

    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Map<String, Map<String, String>> getProperties() {
        return this.properties.getProperties();
    }

    public void setProperties(Map<String, Map<String, String>> properties) {
        this.properties.setProperties(properties);
    }

    public void addProperty(String name, Map<String, String> property) {
        this.properties.addProperty(name, property);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FireDiamond getFireDiamond() {
        return fireDiamond;
    }

    public void setFireDiamond(FireDiamond fireDiamond) {
        this.fireDiamond = fireDiamond;
    }
}

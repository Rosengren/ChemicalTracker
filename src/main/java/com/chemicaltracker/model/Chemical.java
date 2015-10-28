package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

public class Chemical {

    private String name;
    private FireDiamond fireDiamond;
    private Map<String, Map<String, String>> properties;

    public Chemical() {
        initProperties();
        // initialize with blank values
        fireDiamond = new FireDiamond();
        name = "";
    }

    public Chemical(String name) {
        this.name = name;
        initProperties();
    }

    public Chemical(String name, FireDiamond fireDiamond) {
        this(name);
        this.fireDiamond = fireDiamond;
    }

    public Chemical(String name, FireDiamond fireDiamond, Map<String, Map<String, String>> properties) {
        this.name = name;
        this.fireDiamond = fireDiamond;
        this.properties = properties;
    }

    public Map<String, Map<String, String>> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Map<String, String>> properties) {
        this.properties = properties;
    }

    public void addProperty(String name, Map<String, String> property) {
        properties.put(name, property);
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

    private void initProperties() {
        Map<String, String> fireAndExplosionData = new HashMap<String, String>();
        fireAndExplosionData.put("Hazard In Presence Of Other Substances", "");
        fireAndExplosionData.put("Other Details", "");

        Map<String, String> handlingAndStorage = new HashMap<String, String>();
        handlingAndStorage.put("Precautions", "");
        handlingAndStorage.put("Storage", "");

        Map<String, String> physicalAndChemicalProperties = new HashMap<String, String>();
        physicalAndChemicalProperties.put("Boiling Point", "");
        physicalAndChemicalProperties.put("Melting Point", "");

        Map<String, String> firstAidMeasures = new HashMap<String, String>();
        firstAidMeasures.put("Eye Contact", "");
        firstAidMeasures.put("Skin Contact", "");
        firstAidMeasures.put("Inhalation", "");
        firstAidMeasures.put("Ingestion", "");

        Map<String, String> otherProperties = new HashMap<String, String>();
        otherProperties.put("Potential Health Effects", "");
        otherProperties.put("Exposure Controls", "");

        properties = new HashMap<String, Map<String, String>>();
        properties.put("Fire And Explosion Data", fireAndExplosionData);
        properties.put("Handling And Storage", handlingAndStorage);
        properties.put("Physical And Chemical Properties", physicalAndChemicalProperties);
        properties.put("First Aid Measures", firstAidMeasures);
        properties.put("Other Properties", otherProperties);
    }
}

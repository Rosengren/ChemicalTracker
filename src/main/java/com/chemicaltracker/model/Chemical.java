package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Chemical Form:
 *
 * {
 *  Name: "",
 *  Fire Diamond {
 *      Flammability: #,
 *      Health: #,
 *      Instability: #,
 *      Notice: ""
 *  },
 *  Fire and Explosion Data {
 *      Hazard in presence of: "",
 *      Other Details: ""
 *  },
 *  Handling and Storage {
 *      Precautions: "",
 *      Storage: ""
 *  },
 *  Physical and Chemical Properties {
 *      Boiling Point: "",
 *      Melting Point: "",
 *  },
 *  Other Properties {
 *      Potential Health Effects: "",
 *      First Aid Measures: "",
 *      Exposure Controls: ""
 *  }
 * }
**/

public class Chemical {

    private String name;
    private FireDiamond fireDiamond;
    private Map<String, Object> properties;

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

    public Chemical(String name, FireDiamond fireDiamond, Map<String, Object> properties) {
        this.name = name;
        this.fireDiamond = fireDiamond;
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String name, Object property) {
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
        fireAndExplosionData.put("HazardInPresenceOf", "");
        fireAndExplosionData.put("OtherDetails", "");

        Map<String, String> handlingAndStorage = new HashMap<String, String>();
        handlingAndStorage.put("Precautions", "");
        handlingAndStorage.put("Storage", "");

        Map<String, String> physicalAndChemicalProperties = new HashMap<String, String>();
        physicalAndChemicalProperties.put("BoilingPoint", "");
        physicalAndChemicalProperties.put("MeltingPoint", "");

        Map<String, String> otherProperties = new HashMap<String, String>();
        otherProperties.put("PotentialHealthEffects", "");
        otherProperties.put("FirstAidMeasures", "");
        otherProperties.put("ExposureControls", "");

        properties = new HashMap<String, Object>();
        properties.put("FireAndExplosionData", fireAndExplosionData);
        properties.put("HandlingAndStorage", handlingAndStorage);
        properties.put("PhysicalAndChemicalProperties", physicalAndChemicalProperties);
        properties.put("OtherProperties", otherProperties);
    }
}

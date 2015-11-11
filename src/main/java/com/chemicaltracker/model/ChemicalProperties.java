package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

public class ChemicalProperties {

    private Map<String, Map<String, String>> properties;

    public ChemicalProperties() {
        initProperties();
    }

    public ChemicalProperties(final Map<String, Map<String, String>> properties) {
        this.properties = properties;
    }

    public void setProperties(final Map<String, Map<String, String>> properties) {
        this.properties = properties;
    }

    public Map<String, Map<String, String>> getProperties() {
        return this.properties;
    }

    public void addProperty(final String name, Map<String, String> property) {
        properties.put(name, property);
    }

    public void removeProperty(final String name) {
        properties.remove(name);
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

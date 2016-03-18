package com.chemicaltracker.persistence.model;

import java.util.Map;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class Chemical extends AbstractStorageComponent implements StorageComponent {

    // DB field names
    public static final String PLACEHOLDER_IMAGE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";

    @SerializedName(value = "Name")
    private String name;

    private transient String imageURL;
    private transient FireDiamond fireDiamond;
    private transient Boolean match;

    @SerializedName(value = "Accidental Release Measures")
    private Map<String, String> accidentalReleaseMeasures;

    @SerializedName(value = "Disposal Considerations")
    private Map<String, String> disposalConsiderations;

    @SerializedName(value = "Ecological Information")
    private Map<String, String> ecologicalInformation;

    @SerializedName(value = "Exposure Controls and Personal Protection")
    private Map<String, String> exposureControlsAndPersonalProtection;

    @SerializedName(value = "Fire and Explosion Data")
    private Map<String, String> fireAndExplosionData;

    @SerializedName(value = "First Aid Measures")
    private Map<String, String> firstAidMeasures;

    @SerializedName(value = "Handling and Storage")
    private Map<String, String> handlingAndStorage;

    @SerializedName(value = "Hazards Identification")
    private Map<String, String> hazardsIdentification;

    @SerializedName(value = "Physical and Chemical Properties")
    private Map<String, String> physicalAndChemicalProperties;

    @SerializedName(value = "Stability and Reactivity Data")
    private Map<String, String> stabilityAndReactivityData;

    @SerializedName(value = "Toxicological Information")
    private Map<String, String> toxicologicalInformation;

    @SerializedName(value = "Transport Information")
    private Map<String, String> transportInformation;

    @SerializedName(value = "Flammability")
    private int flammability;

    @SerializedName(value = "Health")
    private int health;

    @SerializedName(value = "Instability")
    private int instability;

    @SerializedName(value = "Notice")
    private String notice;


    public Chemical() {
        this.fireDiamond = new FireDiamond();
        this.name = "";
        this.match = true;
        this.imageURL = PLACEHOLDER_IMAGE_URL;

        flammability = 0;
        health = 0;
        instability = 0;
        notice = "";
    }

    @Override
    public String getDescription() {
        String desc = "Flammability: " + fireDiamond.getFlammability()
                + ", Health: " + fireDiamond.getHealth()
                + ", Instability: " + fireDiamond.getInstability()
                + ", Notice: " + fireDiamond.getNotice();

        desc += "\n\nStorage:\n" + handlingAndStorage.get("Storage");
        return desc;
    }

    public String getName() { return name; }
    public void setName(String value) { name = value; }

    public Map<String, String> getAccidentalReleaseMeasures() {
        return accidentalReleaseMeasures;
    }
    public void setAccidentalReleaseMeasures(Map<String, String> value) {
        accidentalReleaseMeasures = value;
    }

    public Map<String, String> getDisposalConsiderations() {
        return disposalConsiderations;
    }
    public void setDisposalConsiderations(Map<String, String> value) {
        disposalConsiderations = value;
    }

    public Map<String, String> getEcologicalInformation() {
        return ecologicalInformation;
    }
    public void setEcologicalInformation(Map<String, String> value) {
        ecologicalInformation = value;
    }

    public Map<String, String> getExposureControlsAndPersonalProtection() {
        return exposureControlsAndPersonalProtection;
    }
    public void setExposureControlsAndPersonalProtection(Map<String, String> value) {
        exposureControlsAndPersonalProtection = value;
    }

    public Map<String, String> getFireAndExplosionData() {
        return fireAndExplosionData;
    }
    public void setFireAndExplosionData(Map<String, String> value) {
        fireAndExplosionData = value;
    }

    public Map<String, String> getFirstAidMeasures() {
        return firstAidMeasures;
    }
    public void setFirstAidMeasures(Map<String, String> value) {
        firstAidMeasures = value;
    }

    public Map<String, String> getHandlingAndStorage() {
        return handlingAndStorage;
    }
    public void setHandlingAndStorage(Map<String, String> value) {
        handlingAndStorage = value;
    }

    public Map<String, String> getHazardsIdentification() {
        return hazardsIdentification;
    }
    public void setHazardsIdentification(Map<String, String> value) {
        hazardsIdentification = value;
    }

    public Map<String, String> getPhysicalAndChemicalProperties() {
        return physicalAndChemicalProperties;
    }
    public void setPhysicalAndChemicalProperties(Map<String, String> value) {
        physicalAndChemicalProperties = value;
    }

    public Map<String, String> getStabilityAndReactivityData() {
        return stabilityAndReactivityData;
    }
    public void setStabilityAndReactivityData(Map<String, String> value) {
        stabilityAndReactivityData = value;
    }

    public Map<String, String> getToxicologicalInformation() {
        return toxicologicalInformation;
    }
    public void setToxicologicalInformation(Map<String, String> value) {
        toxicologicalInformation = value;
    }

    public Map<String, String> getTransportInformation() {
        return transportInformation;
    }
    public void setTransportInformation(Map<String, String> value) {
        transportInformation = value;
    }

    public int getFlammability() { return fireDiamond.getFlammability(); }
    public void setFlammability(int value) { fireDiamond.setFlammability(value); }

    public int getHealth() { return fireDiamond.getHealth(); }
    public void setHealth(int value) { fireDiamond.setHealth(value); }

    public int getInstability() { return fireDiamond.getInstability(); }
    public void setInstability(int value) { fireDiamond.setInstability(value); }

    public String getNotice() { return fireDiamond.getNotice(); }
    public void setNotice(String value) { fireDiamond.setNotice(value); }

    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }

    public Map<String, Map<String, String>> getProperties() {
        Map<String, Map<String, String>> properties = new HashMap<>();

        properties.put("Accidental Release Measures", accidentalReleaseMeasures);
        properties.put("Disposal Considerations", disposalConsiderations);
        properties.put("Ecological Information", ecologicalInformation);
        properties.put("Exposure Controls and Personal Protection", exposureControlsAndPersonalProtection);
        properties.put("Fire and Explosion Data", fireAndExplosionData);
        properties.put("First Aid Measures", firstAidMeasures);
        properties.put("Handling and Storage", handlingAndStorage);
        properties.put("Hazards Identification", hazardsIdentification);
        properties.put("Physical and Chemical Properties", physicalAndChemicalProperties);
        properties.put("Stability and Reactivity Data", stabilityAndReactivityData);
        properties.put("Toxicological Information", toxicologicalInformation);
        properties.put("Transport Information", transportInformation);
        return properties;
    }

    public FireDiamond getFireDiamond() {
        return new FireDiamond(flammability, instability, health, notice);
    }

    public Boolean getMatch() { return match; }
    public void setMatch(final Boolean match) { this.match = match; }
    public Chemical withMatch(boolean match) { this.match = match; return this; }
}

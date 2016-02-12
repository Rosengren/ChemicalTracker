package com.chemicaltracker.persistence.model;

import java.util.Map;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

// Composite Design Pattern: Leaf
@DynamoDBTable(tableName="Chemicals")
public class Chemical extends AbstractStorageComponent implements StorageComponent {

    // DB field names
    public static final String PLACEHOLDER_IMAGE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";

    private String name;
    private String imageURL;
    private FireDiamond fireDiamond;
    private Boolean match;

    private String cas;

    private Map<String, String> accidentalReleaseMeasures;
    private Map<String, String> disposalConsiderations;
    private Map<String, String> ecologicalInformation;
    private Map<String, String> exposureControlsAndPersonalProtection;
    private Map<String, String> fireAndExplosionData;
    private Map<String, String> firstAidMeasures;
    private Map<String, String> handlingAndStorage;
    private Map<String, String> hazardsIdentification;
    private Map<String, String> physicalAndChemicalProperties;
    private Map<String, String> stabilityAndReactivityData;
    private Map<String, String> toxicologicalInformation;
    private Map<String, String> transportInformation;


    public Chemical() {
        this.fireDiamond = new FireDiamond();
        this.name = "";
        this.match = true;
        this.imageURL = PLACEHOLDER_IMAGE_URL;
    }

    @Override
    @DynamoDBIgnore
    public String getDescription() {
        String desc = "Flammability: " + fireDiamond.getFlammability()
                + ", Health: " + fireDiamond.getHealth()
                + ", Instability: " + fireDiamond.getInstability()
                + ", Notice: " + fireDiamond.getNotice();

        desc += "\n\nStorage:\n" + handlingAndStorage.get("Storage");
        return desc;
    }

    @DynamoDBHashKey(attributeName="Name")
    public String getName() { return name; }
    public void setName(String value) { name = value; }

    @DynamoDBAttribute(attributeName="CAS")
    public String getCas() { return cas; }
    public void setCas(String value) { cas = value; }

    @DynamoDBAttribute(attributeName="Accidental Release Measures")
    public Map<String, String> getAccidentalReleaseMeasures() {
        return accidentalReleaseMeasures;
    }
    public void setAccidentalReleaseMeasures(Map<String, String> value) {
        accidentalReleaseMeasures = value;
    }

    @DynamoDBAttribute(attributeName="Disposal Considerations")
    public Map<String, String> getDisposalConsiderations() {
        return disposalConsiderations;
    }
    public void setDisposalConsiderations(Map<String, String> value) {
        disposalConsiderations = value;
    }

    @DynamoDBAttribute(attributeName="Ecological Information")
    public Map<String, String> getEcologicalInformation() {
        return ecologicalInformation;
    }
    public void setEcologicalInformation(Map<String, String> value) {
        ecologicalInformation = value;
    }

    @DynamoDBAttribute(attributeName="Exposure Controls and Personal Protection")
    public Map<String, String> getExposureControlsAndPersonalProtection() {
        return exposureControlsAndPersonalProtection;
    }
    public void setExposureControlsAndPersonalProtection(Map<String, String> value) {
        exposureControlsAndPersonalProtection = value;
    }

    @DynamoDBAttribute(attributeName="Fire and Explosion Data")
    public Map<String, String> getFireAndExplosionData() {
        return fireAndExplosionData;
    }
    public void setFireAndExplosionData(Map<String, String> value) {
        fireAndExplosionData = value;
    }
    @DynamoDBAttribute(attributeName="First Aid Measures")
    public Map<String, String> getFirstAidMeasures() {
        return firstAidMeasures;
    }
    public void setFirstAidMeasures(Map<String, String> value) {
        firstAidMeasures = value;
    }

    @DynamoDBAttribute(attributeName="Handling and Storage")
    public Map<String, String> getHandlingAndStorage() {
        return handlingAndStorage;
    }
    public void setHandlingAndStorage(Map<String, String> value) {
        handlingAndStorage = value;
    }

    @DynamoDBAttribute(attributeName="Hazards Identification")
    public Map<String, String> getHazardsIdentification() {
        return hazardsIdentification;
    }
    public void setHazardsIdentification(Map<String, String> value) {
        hazardsIdentification = value;
    }

    @DynamoDBAttribute(attributeName="Physical and Chemical Properties")
    public Map<String, String> getPhysicalAndChemicalProperties() {
        return physicalAndChemicalProperties;
    }
    public void setPhysicalAndChemicalProperties(Map<String, String> value) {
        physicalAndChemicalProperties = value;
    }

    @DynamoDBAttribute(attributeName="Stability and Reactivity Data")
    public Map<String, String> getStabilityAndReactivityData() {
        return stabilityAndReactivityData;
    }
    public void setStabilityAndReactivityData(Map<String, String> value) {
        stabilityAndReactivityData = value;
    }

    @DynamoDBAttribute(attributeName="Toxicological Information")
    public Map<String, String> getToxicologicalInformation() {
        return toxicologicalInformation;
    }
    public void setToxicologicalInformation(Map<String, String> value) {
        toxicologicalInformation = value;
    }

    @DynamoDBAttribute(attributeName="Transport Information")
    public Map<String, String> getTransportInformation() {
        return transportInformation;
    }
    public void setTransportInformation(Map<String, String> value) {
        transportInformation = value;
    }

    @DynamoDBAttribute(attributeName="Flammability")
    public int getFlammability() { return fireDiamond.getFlammability(); }
    public void setFlammability(int value) { fireDiamond.setFlammability(value); }

    @DynamoDBAttribute(attributeName="Health")
    public int getHealth() { return fireDiamond.getHealth(); }
    public void setHealth(int value) { fireDiamond.setHealth(value); }

    @DynamoDBAttribute(attributeName="Instability")
    public int getInstability() { return fireDiamond.getInstability(); }
    public void setInstability(int value) { fireDiamond.setInstability(value); }

    @DynamoDBAttribute(attributeName="Notice")
    public String getNotice() { return fireDiamond.getNotice(); }
    public void setNotice(String value) { fireDiamond.setNotice(value); }

    @DynamoDBIgnore
    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }

    @DynamoDBIgnore
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

    @DynamoDBIgnore
    public FireDiamond getFireDiamond() { return fireDiamond; }
    public void setFireDiamond(FireDiamond fireDiamond) { this.fireDiamond = fireDiamond; }

    @DynamoDBIgnore
    public Boolean getMatch() { return match; }
    public void setMatch(final Boolean match) { this.match = match; }
    public Chemical withMatch(boolean match) { this.match = match; return this; }
}

package com.chemicaltracker.persistence.model;

import java.util.*;
import java.util.Map.*;

import com.amazonaws.metrics.MetricInputStreamEntity;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.chemicaltracker.model.StorageTag;

// Annotations


@DynamoDBTable(tableName="Cabinets")
public class Cabinet extends AbstractStorageComponent implements StorageComponent {

    private String id;
    private String name;
    private String username;
    private String description;
    private String imageURL;
    private Set<StorageTag> tags;
    private Map<String, String> chemicalNames;
    private Metrics metrics;

    public Cabinet() {
        super();
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        tags = new HashSet<>();
        chemicalNames = new HashMap<>();
        tags.add(StorageTag.IGNORE); // can't be blank
        metrics = new Metrics();
    }

    @DynamoDBHashKey(attributeName = "Username")
    public String getUsername() { return this.username; }
    public void setUsername(final String username) { this.username = username; }
    public Cabinet withUsername(final String username) { setUsername(username); return this; }

    @DynamoDBRangeKey(attributeName = "Cabinet ID")
    public String getID() { return this.id; }
    public void setID(final String id) { this.id = id; }
    public Cabinet withID(final String id) { setID(id); return this; }

    @DynamoDBAttribute(attributeName = "Image URL")
    public String getImageURL() { return this.imageURL; }
    public void setImageURL(final String imageURL) { this.imageURL = imageURL; }
    public Cabinet withImageURL(final String imageURL) { setImageURL(imageURL); return this; }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }
    public Cabinet withName(final String name) { setName(name); return this; }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }
    public Cabinet withDescription(final String desc) { setDescription(desc); return this; }

    @DynamoDBAttribute(attributeName = "Chemical Names")
    public Map<String, String> getChemicalNames() { return this.chemicalNames; }
    public void setChemicalNames(final Map<String, String> chemicalNames) { this.chemicalNames = chemicalNames; }

    @DynamoDBAttribute(attributeName = "Tags") // TODO: replace setTags
    public Set<String> getTagNames() {
        Set<String> tagNames = new HashSet<>();
        for (StorageTag tag : tags) {
            tagNames.add(tag.name());
        }
        return tagNames;
    }

    public void setTagNames(final Set<String> tags) {
        setTags(tags);
    }

    @DynamoDBAttribute(attributeName = "Metrics")
    public Metrics getMetrics() { return this.metrics; }
    public void setMetrics(final Metrics metrics) { this.metrics = metrics; }

    @DynamoDBDocument
    public static class Metrics {

        private int acidCount = 0;
        private int baseCount = 0;
        private int oxidizerCount = 0;
        private int reductorCount = 0;
        private int chemicalCount = 0;
        private int flammableCount = 0;
        private int unstableCount = 0;
        private int healthHazardCount = 0;

        @DynamoDBAttribute(attributeName = "Acid Count")
        public int getAcidCount() {
            return acidCount;
        }
        public void setAcidCount(int acidCount) {
            this.acidCount = acidCount;
        }

        @DynamoDBAttribute(attributeName = "Base Count")
        public int getBaseCount() {
            return baseCount;
        }
        public void setBaseCount(int baseCount) {
            this.baseCount = baseCount;
        }

        @DynamoDBAttribute(attributeName = "Oxidizer Count")
        public int getOxidizerCount() {
            return oxidizerCount;
        }
        public void setOxidizerCount(int oxidizerCount) {
            this.oxidizerCount = oxidizerCount;
        }

        @DynamoDBAttribute(attributeName = "Reductor Count")
        public int getReductorCount() {
            return reductorCount;
        }
        public void setReductorCount(int reductorCount) {
            this.reductorCount = reductorCount;
        }

        @DynamoDBAttribute(attributeName = "Chemical Count")
        public int getChemicalCount() {
            return chemicalCount;
        }
        public void setChemicalCount(int chemicalCount) {
            this.chemicalCount = chemicalCount;
        }

        @DynamoDBAttribute(attributeName = "Flammable Count")
        public int getFlammableCount() {
            return flammableCount;
        }
        public void setFlammableCount(int flammableCount) {
            this.flammableCount = flammableCount;
        }

        @DynamoDBAttribute(attributeName = "Unstable Count")
        public int getUnstableCount() {
            return unstableCount;
        }
        public void setUnstableCount(int unstableCount) {
            this.unstableCount = unstableCount;
        }

        @DynamoDBAttribute(attributeName = "Health Hazard Count")
        public int getHealthHazardCount() {
            return healthHazardCount;
        }
        public void setHealthHazardCount(int healthHazardCount) {
            this.healthHazardCount = healthHazardCount;
        }
    }

    @DynamoDBIgnore
    public void addChemical(final String chemicalID, final String chemicalName) {
        this.chemicalNames.put(chemicalID, chemicalName);
    }

    @DynamoDBIgnore
    public void removeChemical(final String chemicalID) {
        this.chemicalNames.remove(chemicalID);
    }

    @DynamoDBIgnore
    public void addTag(final StorageTag tag) {
        tags.add(tag);
    }

    @DynamoDBIgnore
    public void addTag(final String tag) {
        if (StorageTag.contains(tag)) {
            this.tags.add(StorageTag.valueOf(tag));
        }
    }

    public void setTags(final Set<String> tags) { // TODO: simplify
        for (String tagName : tags) {
            addTag(tagName);
        }
    }

    @DynamoDBIgnore
    public List<StorageTag> getTags() {
        List<StorageTag> tagList = new ArrayList<>();
        tagList.addAll(tags);
        return tagList;
    }

    @DynamoDBIgnore
    public String getStoredItemID(final String name) {
        return chemicalNames.get(name);
    }

    @DynamoDBIgnore
    public List<String> getStoredItemNames() {
        return new ArrayList<>(chemicalNames.keySet());
    }
}
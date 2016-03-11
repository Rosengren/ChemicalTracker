package com.chemicaltracker.persistence.model;

import java.util.*;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.chemicaltracker.model.StorageTag;

@DynamoDBTable(tableName="Cabinets")
public class Cabinet extends AbstractStorageComponent implements StorageComponent {

    private String id;
    private String name;
    private String username;
    private String description;
    private String imageURL;
    private Map<String, AuditVersion> auditVersions;

    public Cabinet() {
        super();
        imageURL = "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png";
        auditVersions = new HashMap<>();
    }

    @DynamoDBIgnore
    public List<String> getAuditVersionNames() {
        return new ArrayList<>(auditVersions.keySet());
    }

    @DynamoDBIgnore
    public String getCurrentAuditVersionName() {
        AuditVersion latest = getLatestAuditVersion();
        return latest.getName();
    }

    @DynamoDBIgnore
    public AuditVersion getAuditVersion(final String version) {
        return auditVersions.get(version.toLowerCase());
    }

    @DynamoDBIgnore
    public void forkVersion(final String version) {
        final AuditVersion fork = getLatestAuditVersion().clone();
        fork.setName(version);
        auditVersions.put(version, fork);
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

    public void setTagNames(final Set<String> tags) {
        AuditVersion latest = getLatestAuditVersion();
        latest.setTags(tags);
    }

    @DynamoDBAttribute(attributeName = "Audit Versions")
    public Map<String, AuditVersion> getAuditVersions() { return this.auditVersions; }
    public void setAuditVersions(Map<String, AuditVersion> auditVersions) { this.auditVersions = auditVersions; }
    public Cabinet withAuditVersion(final String name) {
        auditVersions.put(name.toLowerCase(), new AuditVersion().withName(name));
        return this;
    }

    @DynamoDBIgnore
    public Metrics getMetrics() {
        AuditVersion latest = getLatestAuditVersion();
        return latest.getMetrics();
    }
    public void setMetrics(final Metrics metrics) {
        AuditVersion latest = getLatestAuditVersion();
        latest.setMetrics(metrics);
    }

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

    @DynamoDBDocument
    public static class AuditVersion implements Cloneable {

        private long timestamp;
        private String name;
        private Map<String, String> chemicalNames;
        private Set<StorageTag> tags;
        private Metrics metrics;

        public AuditVersion() {
            updateTimestamp();
            chemicalNames = new HashMap<>();
            metrics = new Metrics();
            tags = new HashSet<>();
            tags.add(StorageTag.IGNORE);
        }

        @DynamoDBIgnore
        @Override
        public AuditVersion clone() {
            final AuditVersion clone = new AuditVersion();
            clone.setName(getName());
            clone.setChemicals(getChemicals());
            clone.setMetrics(getMetrics());
            clone.setStorageTags(tags);
            return clone;
        }

        @DynamoDBAttribute(attributeName = "Name")
        public String getName() { return this.name; }
        public void setName(final String name) { this.name = name; }
        public AuditVersion withName(final String name) { this.name = name; return this; }

        @DynamoDBAttribute(attributeName = "Timestamp")
        public long getTimestamp() { return this.timestamp; }
        public void setTimestamp(final long timestamp) { this.timestamp = timestamp; }

        @DynamoDBAttribute(attributeName = "Chemicals")
        public Map<String, String> getChemicals() { return this.chemicalNames; }
        public void setChemicals(final Map<String, String> chemicalNames) { this.chemicalNames = chemicalNames; }

        @DynamoDBAttribute(attributeName = "Metrics")
        public Metrics getMetrics() { return this.metrics; }
        public void setMetrics(final Metrics metrics) { this.metrics = metrics; }

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

        public void setStorageTags(final Set<StorageTag> tags) { // TODO: simplify
            this.tags = tags;
        }

        public void setTags(final Set<String> tags) {
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
        public void addChemical(final String chemicalID, final String chemicalName) {
            this.chemicalNames.put(chemicalID, chemicalName);
        }

        @DynamoDBIgnore
        private void updateTimestamp() {
            timestamp = System.currentTimeMillis();
        }

        @DynamoDBIgnore
        public void removeChemical(final String chemicalID) {
            this.chemicalNames.remove(chemicalID);
        }

        @DynamoDBIgnore
        public List<String> getChemicalNames() {
            return new ArrayList<String>(this.chemicalNames.keySet());
        }

        @DynamoDBIgnore
        public String getStoredItemID(final String name) {
            return chemicalNames.get(name);
        }

    }

    @DynamoDBIgnore
    public void addChemical(final String auditName, final String chemicalID, final String chemicalName) {
        AuditVersion version = auditVersions.get(auditName);
        if (version == null) {
            version = new AuditVersion().withName("VERSION 1 - add Chemical"); // TODO: make the default version more explicit
            auditVersions.put(auditName, version);
        }
        version.addChemical(chemicalID, chemicalName);
    }

    @DynamoDBIgnore
    public void removeChemical(final String auditName, final String chemicalID) {
        AuditVersion version = auditVersions.get(auditName);
        if (version != null) {
            version.removeChemical(chemicalID);
        }
    }

    @DynamoDBIgnore
    public String getStoredItemID(final String name) {
        AuditVersion version = getLatestAuditVersion();
        return version.getStoredItemID(name);
    }

    @DynamoDBIgnore
    public List<String> getStoredItemNames() {
        AuditVersion version = getLatestAuditVersion();
        if (version == null) {
            return new ArrayList<>();
        }
        return version.getChemicalNames();
    }

    @DynamoDBIgnore
    public void addTag(final StorageTag tag) {
        AuditVersion latest = getLatestAuditVersion();
        latest.addTag(tag);
    }

    @DynamoDBIgnore
    public void addTag(final String tag) {
        AuditVersion latest = getLatestAuditVersion();
        latest.addTag(tag);
    }

    public void setTags(final Set<String> tags) { // TODO: simplify
        for (String tagName : tags) {
            addTag(tagName);
        }
    }

    @DynamoDBIgnore
    public List<StorageTag> getTags() {
        AuditVersion latest = getLatestAuditVersion();
        return latest.getTags();
    }

    @DynamoDBIgnore
    public AuditVersion getLatestAuditVersion() {
        AuditVersion latest = null;
        long t = 0;
        for (AuditVersion v : auditVersions.values()) {
            if (v.getTimestamp() > t) {
                t = v.getTimestamp();
                latest = v;
            }
        }

        if (latest == null) {
            // Create empty one
            latest = new AuditVersion();
            latest.setName("VERSION 1 - getLatest".toLowerCase());
            auditVersions.put("VERSION 1 - getLatest".toLowerCase(), latest); // TODO: add requirement that user must pass version name
            return latest;
        }

        return latest;
    }

    @DynamoDBIgnore
    public void addAuditVersion(final String name, final AuditVersion auditVersion) {
        this.auditVersions.put(name, auditVersion);
    }
}
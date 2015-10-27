package com.chemicaltracker.model;

import java.util.List;
import java.util.ArrayList;

public class Container {

    // TODO: maybe add a field to determine if this is a new container or an existing container
    private String username;
    private String containerName;
    private String description;
    private List<String> chemicalNames;

    // Used in the create container page
    public Container() {

    }

    public Container(final String username, final String containerName, final String description) {
        this.username = username;
        this.containerName = containerName;
        this.description = description;
    }

    public Container(final String username, final String containerName,
            final String description, final List<String> chemicalNames) {
        this(username, containerName, description);
        this.chemicalNames = chemicalNames;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setContainerName(final String containerName) {
        this.containerName = containerName;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setChemicalNames(final List<String> chemicalNames) {
        this.chemicalNames = chemicalNames;
    }

    public void addChemicalName(final String chemicalName) {
        this.chemicalNames.add(chemicalName);
    }

    public List<String> getChemicalNames() {
        return this.chemicalNames;
    }
}

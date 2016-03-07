package com.chemicaltracker.controller.api.response;

import java.util.HashSet;
import java.util.Set;

public class CompareCabinetsResponse {

    private Set<String> added;
    private Set<String> removed;
    private Set<String> matching;
    private String oldVersion;
    private String newVersion;

    public CompareCabinetsResponse() {
        added = new HashSet<>();
        removed = new HashSet<>();
        matching = new HashSet<>();
    }

    public Set<String> getAdded() {
        return added;
    }

    public void setAdded(Set<String> added) {
        this.added = added;
    }

    public Set<String> getRemoved() {
        return removed;
    }

    public void setRemoved(Set<String> removed) {
        this.removed = removed;
    }

    public Set<String> getMatching() {
        return matching;
    }

    public void setMatching(Set<String> matching) {
        this.matching = matching;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}

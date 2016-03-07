package com.chemicaltracker.model;

import java.util.HashSet;
import java.util.Set;

public class Comparison {

    private Set<String> addedChemicals;
    private Set<String> removedChemicals;
    private Set<String> matchingChemicals;

    public Comparison() {
        addedChemicals = new HashSet<>();
        removedChemicals = new HashSet<>();
        matchingChemicals = new HashSet<>();
    }

    public Comparison(final Set<String> added, final Set<String> removed, final Set<String> matching) {
        addedChemicals = added;
        removedChemicals = removed;
        matchingChemicals = matching;
    }

    public Set<String> getAddedChemicals() {
        return addedChemicals;
    }

    public Set<String> getRemovedChemicals() {
        return removedChemicals;
    }

    public Set<String> getMatchingChemicals() {
        return matchingChemicals;
    }
}

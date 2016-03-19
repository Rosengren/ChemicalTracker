package com.chemicaltracker.controller.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChemicalQueryResponse {

    private List<String> matches;

    public List<String> getMatches() {
        return matches;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }

    public ChemicalQueryResponse withMatches(List<String> matches) {
        this.matches = matches;
        return this;
    }
}

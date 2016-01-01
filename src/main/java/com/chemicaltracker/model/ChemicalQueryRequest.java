package com.chemicaltracker.model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChemicalQueryRequest {

    private List<String> chemicals;

    public ChemicalQueryRequest() {
        chemicals = new ArrayList<String>();
    }

    public ChemicalQueryRequest(final List<String> chemicals) {
        this.chemicals = chemicals;
    }

    public String toString() {
        return "{ 'chemicals' : " + chemicals.toString() + "}";
    }

    public List<String> getChemicals() {
        return this.chemicals;
    }

    public void setChemicals(List<String> chemicals) {
        this.chemicals = chemicals;
    }
}

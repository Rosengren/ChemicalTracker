package com.chemicaltracker.model.request;

public class ChemicalQueryRequest {

    private String chemical;

    public ChemicalQueryRequest() {
        chemical = "";
    }

    public ChemicalQueryRequest(final String chemical) {
        this.chemical = chemical;
    }

    public String getChemical() { return this.chemical; }
    public void setChemical(String chemical) { this.chemical = chemical; }
}

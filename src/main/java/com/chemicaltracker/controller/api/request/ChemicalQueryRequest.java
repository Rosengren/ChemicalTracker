package com.chemicaltracker.controller.api.request;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChemicalQueryRequest {

    private String chemical;
    private List<String> chemicals;

    public String getChemical() { return this.chemical; }
    public void setChemical(String chemical) { this.chemical = chemical; }

    public List<String> getChemicals() { return this.chemicals; }
    public void setChemicals(List<String> chemicals) { this.chemicals = chemicals; }

}
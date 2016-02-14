package com.chemicaltracker.controller.api.response;

import com.chemicaltracker.persistence.model.Chemical;

import java.util.List;

public class ChemicalSearchResponse {

    private List<Chemical> chemicals;

    public ChemicalSearchResponse(List<Chemical> chemicals) {
        this.chemicals = chemicals;
    }

    public List<Chemical> getChemicals() { return chemicals; }

    public void setChemicals(List<Chemical> chemicals) {
        this.chemicals = chemicals;
    }
}

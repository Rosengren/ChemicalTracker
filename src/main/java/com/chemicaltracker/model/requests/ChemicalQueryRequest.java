package com.chemicaltracker.model.requests;

import com.chemicaltracker.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONArray;

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

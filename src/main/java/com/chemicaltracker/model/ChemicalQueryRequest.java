package com.chemicaltracker.model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONArray;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class ChemicalQueryRequest {

    //private List<String> chemicals;
    private String chemical;

    public ChemicalQueryRequest() {
        chemical = "";
        //chemicals = new ArrayList<String>();
    }

    //public ChemicalQueryRequest(final List<String> chemicals) {
    public ChemicalQueryRequest(final String chemical) {
        this.chemical = chemical;
    }

    //public String toString() {
        //return chemical;
        //JSONArray jsArray = new JSONArray(chemicals);
        //return "{ \"chemicals\" : " + JSONArray.toJSONString(chemicals) + "}";
    //}

    public String getChemical() {
        return this.chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }
}

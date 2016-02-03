package com.chemicaltracker.model.requests;

import com.chemicaltracker.model.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoveChemicalRequest {

	private String chemicalName;

	public RemoveChemicalRequest() {
		chemicalName = "";
	}

	public void setChemicalName(String value) {
		chemicalName = value;
	}

	public String getChemicalName() {
		return chemicalName;
	}
}
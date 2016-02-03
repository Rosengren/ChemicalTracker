package com.chemicaltracker.model.requests;

public class PartialChemicalQueryRequest {

	private String chemical;

	public PartialChemicalQueryRequest() {
		chemical = "unknown";
	}

	public PartialChemicalQueryRequest(final String chemical) {
		this.chemical = chemical;
	}

	public String getChemical() { return this.chemical; }
	public void setChemical(String chemical) { this.chemical = chemical; }
}
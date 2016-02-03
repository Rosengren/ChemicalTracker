package com.chemicaltracker.model.response;

import com.chemicaltracker.model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

public class PartialChemicalQueryResponse {

	private List<String> chemicalNames;
	private Boolean match;

	public PartialChemicalQueryResponse() {
		chemicalNames = new ArrayList<String>();
		match = false;
	}

	public PartialChemicalQueryResponse(final List<String> chemicalNames) {
		this.chemicalNames = chemicalNames;
		checkMatch();
	}

	public void checkMatch() {
		match = !chemicalNames.isEmpty();
	}

	public void addChemicalName(String chemicalName) { 
		chemicalNames.add(chemicalName);
		checkMatch();
	}

	public void setChemicalNames(final List<String> chemicalNames) { 
		this.chemicalNames = chemicalNames;
		checkMatch();
	}

	public List<String> getChemicalNames() {
		return this.chemicalNames;
	}

	public Boolean getMatch() { return this.match; }
	public void setMatch(Boolean match) { this.match = match; }

}
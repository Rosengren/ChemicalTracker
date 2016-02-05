package com.chemicaltracker.model.request;

import java.util.*;

public class PartialChemicalQueriesRequest {

	private List<String> chemicals;

	public PartialChemicalQueriesRequest() {
		chemicals = new ArrayList<String>();
	}

	public PartialChemicalQueriesRequest(final List<String> chemicals) {
		setChemicals(chemicals);
	}

	public List<String> getChemicals() { return this.chemicals; }
	public void setChemicals(List<String> chemicals) { this.chemicals = chemicals; }
}
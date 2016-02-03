package com.chemicaltracker.model.response;

import java.util.*;

public class UserTreeResponse {

	private Map<String, Map<String, List<String>>> locations;

	public UserTreeResponse() {
		locations = new HashMap<String, Map<String, List<String>>>();
	}

	public UserTreeResponse(final Map<String, Map<String, List<String>>> locations) {
		this.locations = locations;
	}

	public Map<String, Map<String, List<String>>> getLocations() {
		return locations;
	}

	public void setLocations(final Map<String, Map<String, List<String>>> locations) {
		this.locations = locations;
	}
}
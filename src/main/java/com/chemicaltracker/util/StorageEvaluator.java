package com.chemicaltracker.util;

import java.util.*;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.FireDiamond;
import com.chemicaltracker.model.Storage;

import com.chemicaltracker.persistence.OxidizingAgentDAO;
import com.chemicaltracker.persistence.ReductionAgentDAO;

public class StorageEvaluator {

	private OxidizingAgentDAO oxidizingDB = OxidizingAgentDAO.getInstance();
	private ReductionAgentDAO reductionDB = ReductionAgentDAO.getInstance();

	public StorageEvaluator() {

	}

	public boolean containsOxidizingAgent(final List<String> chemicalNames) {
		return oxidizingDB.containsAgent(chemicalNames);
	}

	public boolean containsReductionAgent(final List<String> chemicalNames) {
		return reductionDB.containsAgent(chemicalNames);
	}

	public boolean isOxidizingAgent(final String chemicalName) {
		return oxidizingDB.isAgent(chemicalName);
	}

	public boolean isReductionAgent(final String chemicalName) {
		return reductionDB.isAgent(chemicalName);
	}

	public boolean containsIncompatibleChemicals(final List<String> chemicalNames) {
		if (containsOxidizingAgent(chemicalNames) &&
			containsReductionAgent(chemicalNames)) {
			return true;
		}
		return false;
	}

	public boolean isAcidic(final Chemical chemical) {
		return getpHProperty(chemical).contains("acid");
	}

	public boolean containsAcids(final List<Chemical> chemicals) {
		for (Chemical chemical : chemicals) {
			if (isAcidic(chemical)) {
				return true;
			}
		}
		return false;
	}

	public boolean isBasic(final Chemical chemical) {
		return getpHProperty(chemical).contains("basic");
	}

	public boolean containsBasics(final List<Chemical> chemicals) {
		for (Chemical chemical : chemicals) {
			if (isBasic(chemical)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsAcidsAndBases(final List<Chemical> chemicals) {
		return containsBasics(chemicals) && containsAcids(chemicals);
	}

	private String getpHProperty(Chemical chemical) {
		final Map<String, String> property = chemical.getProperty("Physical and Chemical Properties");
		return property.get("pH (1% soln/water)").toLowerCase();
	}
}
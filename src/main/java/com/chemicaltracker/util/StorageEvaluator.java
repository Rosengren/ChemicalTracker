package com.chemicaltracker.util;

import java.util.List;

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
}
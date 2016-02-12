package com.chemicaltracker.service;

import com.chemicaltracker.persistence.dao.OxidizingAgentDao;
import com.chemicaltracker.persistence.dao.ReductionAgentDao;
import com.chemicaltracker.persistence.model.Chemical;

import java.util.*;

public class StorageEvaluator {

    private static final OxidizingAgentDao oxidizingDB = OxidizingAgentDao.getInstance();
    private static final ReductionAgentDao reductionDB = ReductionAgentDao.getInstance();

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

    private String getpHProperty(final Chemical chemical) {
        final Map<String, String> property = chemical.getPhysicalAndChemicalProperties();
        return property.get("pH (1% soln/water)").toLowerCase();
    }
}
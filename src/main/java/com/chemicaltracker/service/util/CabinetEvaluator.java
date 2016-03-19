package com.chemicaltracker.service.util;

import com.chemicaltracker.model.StorageTag;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.OxidizingAgentDao;
import com.chemicaltracker.persistence.dao.ReductionAgentDao;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import java.util.*;

// Annotations
import org.springframework.stereotype.Service;

@Service
public class CabinetEvaluator {

    private static final OxidizingAgentDao oxidizingDB = OxidizingAgentDao.getInstance();
    private static final ReductionAgentDao reductionDB = ReductionAgentDao.getInstance();

    private static final ChemicalDao chemicalDB = ChemicalDao.getInstance();

    /**
     * Sets the appropriate Tags and Metrics
     * @param cabinet object to be evaluated
     * @return the cabinet with set tags and metrics
     */
    public Cabinet evaluate(final Cabinet cabinet) {

        cabinet.resetTags(); // remove all tags

        System.out.println("EVALUATING CABINET: " + cabinet.getName());
        System.out.println("HAS: " + cabinet.getStoredItemNames().toString());
        final List<Chemical> chemicals = chemicalDB.findByNames(cabinet.getStoredItemNames());

        if (chemicals.isEmpty()) {
            System.out.println("DIDN'T FIND ANY CHEMICALS");
        } else {
            System.out.println("FOUND " + Arrays.toString(chemicals.toArray()));
        }

        Cabinet.Metrics metrics = cabinet.getMetrics();

        int acidCount = 0;
        int baseCount = 0;
        int flammableCount = 0;
        int unstableCount = 0;
        int healthHazardCount = 0;

        boolean hasBases = false;
        boolean hasAcids = false;
        for (Chemical chemical : chemicals) {

            // Check Flammability
            if (chemical.getFireDiamond().getFlammability() > 0) {
                cabinet.addTag(StorageTag.FLAMMABLE);
                flammableCount++;
            }

            // Check Instability
            if (chemical.getFireDiamond().getInstability() > 0) {
                cabinet.addTag(StorageTag.UNSTABLE);
                unstableCount++;
            }

            // Check Health
            if (chemical.getFireDiamond().getHealth() > 0) {
                cabinet.addTag(StorageTag.HEALTH);
                healthHazardCount++;
            }

            // Contains Bases
            if (isBasic(chemical)) {
                cabinet.addTag(StorageTag.BASIC);
                hasBases = true;
                baseCount++;
            }

            // Contains Acids
            if (isAcidic(chemical)) {
                cabinet.addTag(StorageTag.ACIDIC);
                hasAcids = true;
                acidCount++;
            }
        }

        if (hasAcids && hasBases) {
            cabinet.addTag(StorageTag.ACIDS_AND_BASES);
        }


        boolean hasOxidizingAgents = false;
        int oxidizerCount = oxidizingDB.numberOfAgents(cabinet.getStoredItemNames());
        if (oxidizerCount > 0) {
            cabinet.addTag(StorageTag.OXIDIZING_AGENTS);
            hasOxidizingAgents = true;
        }

        boolean hasReductionAgents = false;
        int reductorCount = reductionDB.numberOfAgents(cabinet.getStoredItemNames());
        if (reductorCount > 0) {
            cabinet.addTag(StorageTag.REDUCTION_AGENTS);
            hasReductionAgents = true;
        }

        if (hasReductionAgents && hasOxidizingAgents) {
            cabinet.addTag(StorageTag.INCOMPATIBLE);
        }

        if (cabinet.getTags().isEmpty()) {
            cabinet.addTag(StorageTag.IGNORE);
        }

        // Set Metrics
        metrics.setChemicalCount(chemicals.size());
        metrics.setFlammableCount(flammableCount);
        metrics.setHealthHazardCount(healthHazardCount);
        metrics.setUnstableCount(unstableCount);
        metrics.setOxidizerCount(oxidizerCount);
        metrics.setReductorCount(reductorCount);
        metrics.setAcidCount(acidCount);
        metrics.setBaseCount(baseCount);

        return cabinet;

    }

    public boolean isAcidic(final Chemical chemical) {
        return getPHProperty(chemical).contains("acid") || chemical.getName().toLowerCase().contains("acid");
    }

    public boolean isBasic(final Chemical chemical) {
        return getPHProperty(chemical).contains("basic");
    }

    private String getPHProperty(final Chemical chemical) {
        final Map<String, String> property = chemical.getPhysicalAndChemicalProperties();
        return property.get("pH (1% soln/water)").toLowerCase();
    }
}
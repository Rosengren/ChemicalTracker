package com.chemicaltracker.database;

import java.util.List;
import java.util.ArrayList;

public class ChemicalDataAccessObjectImpl implements ChemicalDataAccessObject {

    List<Chemical> chemicals;

    public ChemicalDataAccessObjectImpl() {
        chemicals = new ArrayList<Chemical>();

        Chemical testChemical = new Chemical("Hydrochloric Acid");
        Chemical testChemical2 = new Chemical("Ethanol");
        chemicals.add(testChemical);
    }

    @Override
    public List<Chemical> getAllChemicals() {
        // Call Gateway here
        return chemicals;
    }

    @Override
    public Chemical getChemical(String name) {
        for (Chemical c : chemicals) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null; // TODO: should add an exception here
    }

    @Override
    public void updateChemical(Chemical chemical) {
        for (int i = 0; i < chemicals.size(); i++) {
            if (chemicals.get(i).getName().equals(chemical.getName())) {
                chemicals.set(i, chemical);
            }
        }
    }

    @Override
    public void deleteChemical(Chemical chemical) {
        for (int i = 0; i < chemicals.size(); i++) {
            if (chemicals.get(i).getName().equals(chemical.getName())) {
                chemicals.remove(i);
            }
        }
    }
}

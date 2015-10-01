package com.chemicaltracker.database;

import java.util.List;

public interface ChemicalDataAccessObject {

    public List<Chemical> getAllChemicals();
    public Chemical getChemical(String name);
    public void updateChemical(Chemical chemical);
    public void deleteChemical(Chemical chemical);
}

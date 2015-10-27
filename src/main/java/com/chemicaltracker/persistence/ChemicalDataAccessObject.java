package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Chemical;

import java.util.List;

public interface ChemicalDataAccessObject {

    public List<Chemical> getAllChemicals();
    public Chemical getChemical(final String name);
    public void addChemical(final Chemical chemical);
    public void updateChemical(final Chemical chemical);
    public void deleteChemical(final Chemical chemical);

    public List<Chemical> batchGetChemicals(final List<String> names);
}

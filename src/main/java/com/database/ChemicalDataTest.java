package com.chemicaltracker.database;

public class ChemicalDataTest {

    public static void main(String[] args) {
        ChemicalDataAccessObject chemicals = new ChemicalDataAccessObjectImpl();

        // print all chemicals
        for (Chemical c : chemicals.getAllChemicals()) {
            System.out.println("Chemical: " + c.getName());
        }

        // update chemical
        Chemical chemical = chemicals.getAllChemicals().get(0);
        chemical.setName("Something else");
        chemicals.updateChemical(chemical);

        // get the chemical
        chemical = chemicals.getChemical("Something else");
        System.out.println("Got: " + chemical.getName());
    }
}

package com.chemicaltracker.database;

public class ChemicalDataTest {

    public static void main(String[] args) {
        ChemicalDataAccessObject chemicals = new ChemicalDataAccessObjectImpl();

        chemicals.getAllChemicals();
        //FireDiamond fd = new FireDiamond(1, 2, 3, "Dangerous");
        //Chemical chemical = new Chemical("Hydrochloric Acid", fd);
        //chemicals.addChemical(chemical);
        //// print all chemicals
        //for (Chemical c : chemicals.getAllChemicals()) {
            //System.out.println("Chemical: " + c.getName());
        //}

        //// update chemical
        //Chemical chemical = chemicals.getAllChemicals().get(0);
        //chemical.setName("Something else");
        //chemicals.updateChemical(chemical);

        //// get the chemical
        //chemical = chemicals.getChemical("Something else");
        //System.out.println("Got: " + chemical.getName());
    }
}

package com.chemicaltracker.database;

public class Chemical {

    private String name;
    private FireDiamond fireDiamond;

    public Chemical(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FireDiamond getFireDiamond() {
        return fireDiamond;
    }

    public void setFireDiamond(FireDiamond fireDiamond) {
        this.fireDiamond = fireDiamond;
    }
}

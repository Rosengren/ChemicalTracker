package com.chemicaltracker.persistence;

public class Chemical {

    private String name;
    private FireDiamond fireDiamond;

    public Chemical(String name) {
        this.name = name;
    }

    public Chemical(String name, FireDiamond fireDiamond) {
        this.name = name;
        this.fireDiamond = fireDiamond;
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

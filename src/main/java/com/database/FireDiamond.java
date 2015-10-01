package com.chemicaltracker.database;

public class FireDiamond {

    private int flammability;
    private int health;
    private int instability;
    private String notice;

    public FireDiamond() {

    }

    public FireDiamond(int flammability, int health, int instability, String notice) {
        this.flammability = flammability;
        this.health = health;
        this.instability = instability;
        this.notice = notice;
    }

    public int getFlammability() {
        return flammability;
    }

    public void setFlammability(int flammability) {
        this.flammability = flammability;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getInstability() {
        return instability;
    }

    public void setInstability(int instability) {
        this.instability = instability;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}

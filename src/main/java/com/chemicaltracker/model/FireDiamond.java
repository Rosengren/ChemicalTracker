package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

public class FireDiamond {


    public static final String FLAMMABILITY = "Flammability";
    public static final String HEALTH = "Health";
    public static final String INSTABILITY = "Instability";
    public static final String NOTICE = "Notice";

    public static final Integer DEFAULT_VALUE = 0;

    private int flammability;
    private int health;
    private int instability;
    private String notice;

    public FireDiamond() {
        flammability = DEFAULT_VALUE;
        health = DEFAULT_VALUE;
        instability = DEFAULT_VALUE;
        notice = "";
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

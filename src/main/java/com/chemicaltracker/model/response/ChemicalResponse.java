package com.chemicaltracker.model.response;

import com.chemicaltracker.model.*;

public class ChemicalResponse {

	private boolean match;
	private String chemical;
	private Properties properties;

	public ChemicalResponse() {
		match = false;
		chemical = "";
		properties = new Properties();
	}

    public boolean getMatch() { return this.match; }
    public String getChemical() { return this.chemical; }
    public Properties getProperties() { return this.properties; }

    public void setMatch(boolean match) { this.match = match; }
    public void setChemical(String chemical) { this.chemical = chemical; }
    public void setProperties(Properties properties) { this.properties = properties; }

    public ChemicalResponse withMatch(boolean match) { this.match = match; return this; }
    public ChemicalResponse withChemical(String chemical) { this.chemical = chemical; return this; }
    public ChemicalResponse withProperties(Properties properties) { this.properties = properties; return this; }

    public static class Properties {

    	private int health;
    	private int flammability;
    	private int instability;
    	private String notice;

    	public Properties() {
    		health = 0;
    		flammability = 0;
    		instability = 0;
    		notice = "";
    	}

    	public int getHealth() { return this.health; }
    	public int getFlammability() { return this.flammability; }
    	public int getInstability() { return this.instability; }
    	public String getNotice() { return this.notice; }

    	public void setHealth(int health) { this.health = health; }
    	public void setFlammability(int flammability) { this.flammability = flammability; }
    	public void setInstability(int instability) { this.instability = instability; }
    	public void setNotice(String notice) { this.notice = notice; }

    	public Properties withFireDiamond(final FireDiamond diamond) {
    		this.health = diamond.getHealth();
    		this.flammability = diamond.getFlammability();
    		this.instability = diamond.getInstability();
    		this.notice = diamond.getNotice();
    		return this;
    	}
    }

}
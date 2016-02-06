package com.chemicaltracker.model.storage;

public enum StorageTag {

	IGNORE("", "", "", false),
	FLAMMABLE("Flammable", "orange", "Contains flammable chemicals", true),
	UNSTABLE("Unstable", "green", "Contains unstable chemicals", true),
	HEALTH("Health Hazard", "blue", "Contains hazardous chemicals", true),
	INCOMPATIBLE("Incompatible Chemicals", "red", "Warning! Oxidizing Agents and " +
		"Reduction Agents are present in this cabinet!", true),
	OXIDIZING_AGENTS("Oxidizing Agent(s)", "black", "Contains Oxidizing Agents", false),
	REDUCTION_AGENTS("Reduction Agent(s)", "black", "Contains Reduction Agents", false),
	ACIDIC("Acid(s)", "black", "Contains one or more chemicals with a pH level above 7", true),
	BASIC("Base(s)", "black", "Contains one or more chemicals with a pH level below 7", true),
	ACIDS_AND_BASES("Acids and Bases", "red", "Warning! Acids and Bases are present " +
		" in this cabinet!", true);

	private final String title;
	private final String color;
	private final Boolean brief;
	private final String description;

	private StorageTag(final String title, final String color,
		final String description, final Boolean brief) {
		this.title = title;
		this.color = color;
		this.brief = brief;
		this.description = description;
	}

	public String getTitle() {
		return this.title;
	}

	public String getColor() {
		return this.color;
	}

	public Boolean getBrief() {
		return this.brief;
	}

	public String getDescription() {
		return this.description;
	}

	public static boolean contains(String test) {

	    for (StorageTag tag : StorageTag.values()) {
	        if (tag.name().equals(test)) {
	            return true;
	        }
	    }
	    return false;
	}
}
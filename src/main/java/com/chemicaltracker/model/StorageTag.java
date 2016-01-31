package com.chemicaltracker.model;

public enum StorageTag {

	IGNORE("", "", "", false),
	FLAMMABLE("Flammable", "red", "", true),
	UNSTABLE("Unstable", "yellow", "", true),
	HEALTH("A Health Hazard", "blue", "", true),
	INCOMPATIBLE("Incompatible Chemicals", "red", "Warning! Oxidizing Agents and " +
		"Reduction Agents are present in this cabinet!", false),
	OXIDIZING_AGENTS("Oxidizing Agent(s)", "red", "", false),
	REDUCTION_AGENTS("Reduction Agent(s)", "red", "", false);

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
}
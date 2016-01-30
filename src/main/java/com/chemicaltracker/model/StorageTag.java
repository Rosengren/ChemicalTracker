package com.chemicaltracker.model;

public enum StorageTag {

	FLAMMABLE("Flammable", "red"),
	UNSTABLE("Unstable", "yellow"),
	HEALTH("A Health Hazard", "blue");

	private final String title;
	private final String color;

	private StorageTag(final String title, final String color) {
		this.title = title;
		this.color = color;
	}

	public String getTitle() {
		return this.title;
	}

	public String getColor() {
		return this.color;
	}
}
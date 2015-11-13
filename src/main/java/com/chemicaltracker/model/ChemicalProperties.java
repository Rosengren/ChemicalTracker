package com.chemicaltracker.model;

import java.util.Map;
import java.util.HashMap;

public class ChemicalProperties {

    private Map<String, Map<String, String>> properties;

    public ChemicalProperties() {
        initProperties();
    }

    public ChemicalProperties(final Map<String, Map<String, String>> properties) {
        this.properties = properties;
    }

    public void setProperties(final Map<String, Map<String, String>> properties) {
        this.properties = properties;
    }

    public Map<String, Map<String, String>> getProperties() {
        return this.properties;
    }

    public void addProperty(final String name, Map<String, String> property) {
        properties.put(name, property);
    }

    public void removeProperty(final String name) {
        properties.remove(name);
    }

    private void initProperties() {
        Map<String, String> accidentalReleaseMeasures = new HashMap<String, String>();
        accidentalReleaseMeasures.put("Large Spill", "");
        accidentalReleaseMeasures.put("Small Spill", "");

        Map<String, String> disposalConsiderations = new HashMap<String, String>();
        disposalConsiderations.put("Ecotoxicity", "");

        Map<String, String> ecologicalInformation = new HashMap<String, String>();
        ecologicalInformation.put("BOD5 and COD", "");
        ecologicalInformation.put("Ecotoxicity", "");
        ecologicalInformation.put("Products of Biodegradation", "");
        ecologicalInformation.put("Special Remarks on the Producs of Biodegradation", "");
        ecologicalInformation.put("Toxicity of the Products of Biodegradation", "");

        Map<String, String> exposureControls = new HashMap<String, String>();
        exposureControls.put("Engineering Controls", "");
        exposureControls.put("Exposure Limits", "");
        exposureControls.put("Personal Protection", "");
        exposureControls.put("Personal Protection in Case of a Large Spill", "");

        Map<String, String> fireAndExplosionData = new HashMap<String, String>();
        fireAndExplosionData.put("Auto-Ignition Temperature", "");
        fireAndExplosionData.put("Flammability", "");
        fireAndExplosionData.put("Flammable Limits", "");
        fireAndExplosionData.put("Flash Points", "");
        fireAndExplosionData.put("Products of Combustion", "");

        Map<String, String> firstAidMeasures = new HashMap<String, String>();
        firstAidMeasures.put("Eye Contact", "");
        firstAidMeasures.put("Ingestion", "");
        firstAidMeasures.put("Inhalation", "");
        firstAidMeasures.put("Serious Ingestion", "");
        firstAidMeasures.put("Serious Inhalation", "");
        firstAidMeasures.put("Serious Skin Contact", "");
        firstAidMeasures.put("Skin Contact", "");

        Map<String, String> handlingAndStorage = new HashMap<String, String>();
        handlingAndStorage.put("Precautions", "");
        handlingAndStorage.put("Storage", "");

        Map<String, String> hazardsIdentification = new HashMap<String, String>();
        hazardsIdentification.put("Potential Acute Health Effects", "");
        hazardsIdentification.put("Potential Chronic Health Effects", "");

        Map<String, String> physicalAndChemicalProperties = new HashMap<String, String>();
        physicalAndChemicalProperties.put("Boiling Point", "");
        physicalAndChemicalProperties.put("Color", "");
        physicalAndChemicalProperties.put("Critical Temperature", "");
        physicalAndChemicalProperties.put("Ionicity (in Water)", "");
        physicalAndChemicalProperties.put("Melting Point", "");
        physicalAndChemicalProperties.put("Molecular Weight", "");
        physicalAndChemicalProperties.put("Odor", "");
        physicalAndChemicalProperties.put("Odor Threshold", "");
        physicalAndChemicalProperties.put("pH (1% soln/water)", "");
        physicalAndChemicalProperties.put("Physical state and appearance", "");
        physicalAndChemicalProperties.put("Specific Gravity", "");
        physicalAndChemicalProperties.put("Taste", "");
        physicalAndChemicalProperties.put("Vapor Density", "");
        physicalAndChemicalProperties.put("Vapor Pressure", "");
        physicalAndChemicalProperties.put("Volatility", "");
        physicalAndChemicalProperties.put("Water/Oil Dist. Coeff.", "");

        Map<String, String> stabilityAndReactivityData = new HashMap<String, String>();
        stabilityAndReactivityData.put("Conditions of Instability", "");
        stabilityAndReactivityData.put("Corrosivity", "");
        stabilityAndReactivityData.put("Instability Temperature", "");
        stabilityAndReactivityData.put("Polumerization", "");
        stabilityAndReactivityData.put("Special Remarks on Corrosivity", "");
        stabilityAndReactivityData.put("Special Remarks on Reactivity", "");
        stabilityAndReactivityData.put("Stability", "");

        Map<String, String> toxicologicalInformation = new HashMap<String, String>();
        toxicologicalInformation.put("Chronic Effects on Humans", "");
        toxicologicalInformation.put("Other Toxic Effects on Humans", "");
        toxicologicalInformation.put("Routes of Entry", "");
        toxicologicalInformation.put("Special Remarks on Chronic Effects on Humans", "");
        toxicologicalInformation.put("Special Remarks on other Toxic Effects", "");
        toxicologicalInformation.put("Special Remarks on Toxicity to Animals", "");
        toxicologicalInformation.put("Toxicity and Animals", "");

        Map<String, String> transportInformation = new HashMap<String, String>();
        transportInformation.put("DOT Classification", "");
        transportInformation.put("Identification", "");
        transportInformation.put("Special Provisions for Transport", "");

        properties = new HashMap<String, Map<String, String>>();
        properties.put("Accidental Release Measures", accidentalReleaseMeasures);
        properties.put("Disposal Considerations", disposalConsiderations);
        properties.put("Ecological Information", ecologicalInformation);
        properties.put("Exposure Controls and Personal Protection", exposureControls);
        properties.put("Fire and Explosion Data", fireAndExplosionData);
        properties.put("First Aid Measures", firstAidMeasures);
        properties.put("Handling and Storage", handlingAndStorage);
        properties.put("Hazards Identification", hazardsIdentification);
        properties.put("Physical and Chemical Properties", physicalAndChemicalProperties);
        properties.put("Stability and Reactivity Data", stabilityAndReactivityData);
        properties.put("Toxicological Information", toxicologicalInformation);
        properties.put("Transport Information", transportInformation);
    }
}

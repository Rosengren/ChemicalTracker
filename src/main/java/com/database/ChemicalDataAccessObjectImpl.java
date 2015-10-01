package com.chemicaltracker.database;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
//import com.amazonaws.services.dynamodbv2.

public class ChemicalDataAccessObjectImpl implements ChemicalDataAccessObject {

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private AmazonDynamoDBClient dynamoDB;

    private List<Chemical> chemicals;
    private Table chemicalTable;

    public ChemicalDataAccessObjectImpl() {
        try {
            initializeDBConnection();
        } catch (Exception e) {
            System.out.println("Error occured while initializing DB Connection");
        }

        chemicals = new ArrayList<Chemical>();

        //Chemical testChemical = new Chemical("Hydrochloric Acid");
        //Chemical testChemical2 = new Chemical("Ethanol");
        //chemicals.add(testChemical);
    }

    public void initializeDBConnection() throws Exception {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);

        //chemicalTable = dynamoDB.getTable(CHEMICALS_TABLE_NAME);
    }

    @Override
    public List<Chemical> getAllChemicals() {
        // Call Gateway here
        return chemicals;
    }

    @Override
    public Chemical getChemical(final String name) {
        for (Chemical c : chemicals) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null; // TODO: should add an exception here
    }

    @Override
    public void updateChemical(final Chemical chemical) {
        for (int i = 0; i < chemicals.size(); i++) {
            if (chemicals.get(i).getName().equals(chemical.getName())) {
                chemicals.set(i, chemical);
            }
        }
    }

    @Override
    public void deleteChemical(final Chemical chemical) {
        for (int i = 0; i < chemicals.size(); i++) {
            if (chemicals.get(i).getName().equals(chemical.getName())) {
                chemicals.remove(i);
            }
        }
    }

    @Override
    public void addChemical(final Chemical chemical) {
        Map<String, AttributeValue> item = convertChemicalToItem(chemical);
        PutItemRequest putItemRequest = new PutItemRequest(CHEMICALS_TABLE_NAME, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        //PutItemOutcome outcome = table.putItem(item);
    }

    private Map<String, AttributeValue> convertChemicalToItem(final Chemical chemical) {
        FireDiamond fireDiamond = chemical.getFireDiamond();

        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put("Name", new AttributeValue(chemical.getName()));
        item.put("Flamability", new AttributeValue().withN(Integer.toString(fireDiamond.getFlammability())));
        item.put("Health", new AttributeValue().withN(Integer.toString(fireDiamond.getHealth())));
        item.put("Instability", new AttributeValue().withN(Integer.toString(fireDiamond.getInstability())));
        item.put("Notice", new AttributeValue(fireDiamond.getNotice()));

        return item;
    }
}

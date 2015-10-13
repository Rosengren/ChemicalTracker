package com.chemicaltracker.persistence;

import com.chemicaltracker.model.FireDiamond;
import com.chemicaltracker.model.Chemical;

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
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;

public class ChemicalDataAccessDynamoDB implements ChemicalDataAccessObject {

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    private AmazonDynamoDBClient dynamoDB;

    public ChemicalDataAccessDynamoDB() {
        try {
            initializeDBConnection();
        } catch (Exception e) {
            System.out.println("Error occured while initializing DB Connection");
        }
    }

    public void initializeDBConnection() throws Exception {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        dynamoDB = new AmazonDynamoDBClient(credentials);
        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    @Override
    public List<Chemical> getAllChemicals() {
        // TODO: may need a more efficient way of converting these values instead of looping
        final ScanRequest scanRequest = new ScanRequest()
            .withTableName(CHEMICALS_TABLE_NAME);

        final ScanResult result = dynamoDB.scan(scanRequest);
        final List<Chemical> chemicals = new ArrayList<Chemical>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            chemicals.add(convertItemToChemical(item));
        }

        return chemicals;
    }

    @Override
    public Chemical getChemical(final String name) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS(name));

        final GetItemRequest request = new GetItemRequest(CHEMICALS_TABLE_NAME, item);
        final GetItemResult result  = dynamoDB.getItem(request);
        return convertItemToChemical(result.getItem());
    }

    @Override
    public void updateChemical(final Chemical chemical) {
        addChemical(chemical);
    }

    @Override
    public void deleteChemical(final Chemical chemical) {
        //Map<String, AttributeValue> item = convertChemicalToItem(chemical);
        //DeleteItemRequest deleteItemRequest = new DeleteItemRequest(CHEMICALS_TABLE_NAME, item);
        //DeleteItemResult result = dynamoDB.deleteItem(deleteItemRequest);
    }

    @Override
    public void addChemical(final Chemical chemical) {
        // TODO: add check that chemical was correctly added
        final Map<String, AttributeValue> item = convertChemicalToItem(chemical);
        final PutItemRequest putItemRequest = new PutItemRequest(CHEMICALS_TABLE_NAME, item);
        final PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
    }

    // TODO: move this into a new class which handles the conversion
    private Map<String, AttributeValue> convertChemicalToItem(final Chemical chemical) {
        final FireDiamond fireDiamond = chemical.getFireDiamond();
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put(CHEMICALS_TABLE_INDEX, new AttributeValue(chemical.getName()));
        item.put(FireDiamond.FLAMMABILITY, new AttributeValue().withN(Integer.toString(fireDiamond.getFlammability())));
        item.put(fireDiamond.HEALTH, new AttributeValue().withN(Integer.toString(fireDiamond.getHealth())));
        item.put(fireDiamond.INSTABILITY, new AttributeValue().withN(Integer.toString(fireDiamond.getInstability())));
        item.put(fireDiamond.NOTICE, new AttributeValue(fireDiamond.getNotice()));

        return item;
    }

    private Chemical convertItemToChemical(final Map<String, AttributeValue> item) {
        // TODO: add Exception
        if (item == null) {
            // throw exception
            return null;
        }

        final FireDiamond fireDiamond = new FireDiamond(
                Integer.parseInt(item.get(FireDiamond.FLAMMABILITY).getN()),
                Integer.parseInt(item.get(FireDiamond.HEALTH).getN()),
                Integer.parseInt(item.get(FireDiamond.INSTABILITY).getN()),
                item.get(FireDiamond.NOTICE).getS());

        return new Chemical(item.get("Name").getS(), fireDiamond);
    }
}

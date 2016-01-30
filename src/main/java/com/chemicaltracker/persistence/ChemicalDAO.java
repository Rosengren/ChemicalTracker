package com.chemicaltracker.persistence;

import com.chemicaltracker.model.FireDiamond;
import com.chemicaltracker.model.Chemical;

import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;

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

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public class ChemicalDAO {

    private static volatile ChemicalDAO instance;

    private static final Logger logger = Logger.getLogger(ChemicalDAO.class);

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private ChemicalDAO() {

        try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occured while initializing DB Connection", e);
        }
    }

    public static ChemicalDAO getInstance() {

        if (instance == null) {
            synchronized (ChemicalDAO.class) {
                if (instance == null) {
                    instance = new ChemicalDAO();
                }
            }
        }

        return instance;
    }
    public void initializeDBConnection() throws AmazonClientException {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        try {
            amazonDynamoDBClient = new AmazonDynamoDBClient(credentials);
        } catch (Exception e) {
            throw new AmazonClientException("The provided credentials were not valid", e);
        }

        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        amazonDynamoDBClient.setRegion(usWest2);
    }

    public List<Chemical> getAllChemicals() {

        final List<Chemical> chemicals = new ArrayList<Chemical>();
        final ScanRequest scanRequest = new ScanRequest()
            .withTableName(CHEMICALS_TABLE_NAME);

        try {
            final ScanResult result = amazonDynamoDBClient.scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {
                chemicals.add(convertItemToChemical(item));
            }

        } catch (Exception e) {
            logger.error("Error occurred while scanning for all chemicals in table: " + CHEMICALS_TABLE_NAME, e);
        }

        return chemicals;
    }

    public Chemical getChemical(final String name) {

        final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS(name));

        final GetItemRequest request = new GetItemRequest()
            .withTableName(CHEMICALS_TABLE_NAME)
            .withKey(key);

        try {
            final GetItemResult result = amazonDynamoDBClient.getItem(request);

            if (result.getItem() == null) {
                return new Chemical();
            } else {
                return convertItemToChemical(result.getItem());
            }

        } catch (Exception e) {
            logger.error("Error occurred while getting chemical: " + name + " from table: " + CHEMICALS_TABLE_NAME);
        }
        // not sure if returning an empty obect is better than no object.
        // From the user's point of view, at least it will display something
        return new Chemical();
    }

    public void updateChemical(final Chemical chemical) {
        addChemical(chemical);
    }

    public void deleteChemical(final Chemical chemical) {

        final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS(chemical.getName()));

        final DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
            .withTableName(CHEMICALS_TABLE_NAME)
            .withKey(key);

        try {
            amazonDynamoDBClient.deleteItem(deleteItemRequest);
        } catch (AmazonServiceException e) {
            logger.error("Error occured while trying to delete chemical: " +
                    chemical.getName() + " from table: " + CHEMICALS_TABLE_NAME);
        }
    }

    public void addChemical(final Chemical chemical) {

        final Map<String, AttributeValue> item = convertChemicalToItem(chemical);
        final PutItemRequest putItemRequest = new PutItemRequest()
            .withTableName(CHEMICALS_TABLE_NAME)
            .withItem(item);

        try {
            amazonDynamoDBClient.putItem(putItemRequest);
        } catch (AmazonServiceException e) {
            logger.error("Error occurred while trying to add chemical: " +
                    chemical.getName() + " to table: " + CHEMICALS_TABLE_NAME, e);
        }
    }

    public List<String> getAllChemicalNames() {

        final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS("All"));

        final GetItemRequest request = new GetItemRequest()
            .withTableName(CHEMICALS_TABLE_NAME)
            .withKey(key);

        try {
            final GetItemResult result = amazonDynamoDBClient.getItem(request);

            if (result.getItem() != null) {
                return result.getItem().get("Chemicals").getSS();
            }

        } catch (Exception e) {
            logger.error("Error occurred while getting all chemical names from table: " + CHEMICALS_TABLE_NAME);
        }

     
        return new ArrayList<String>();
    }

    public List<Chemical> batchGetChemicals(final List<String> names) {
        final List<Chemical> chemicals = new ArrayList<Chemical>();

        for (String name : names) {
            chemicals.add(getChemical(name));
        }


        return chemicals;
    }

    private Map<String, AttributeValue> convertChemicalToItem(final Chemical chemical) {
        final FireDiamond fireDiamond = chemical.getFireDiamond();
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put(CHEMICALS_TABLE_INDEX, new AttributeValue(chemical.getName()));
        item.put(FireDiamond.FLAMMABILITY, new AttributeValue().withN(Integer.toString(fireDiamond.getFlammability())));
        item.put(fireDiamond.HEALTH, new AttributeValue().withN(Integer.toString(fireDiamond.getHealth())));
        item.put(fireDiamond.INSTABILITY, new AttributeValue().withN(Integer.toString(fireDiamond.getInstability())));
        item.put(fireDiamond.NOTICE, new AttributeValue(fireDiamond.getNotice()));

        try {
            for (Map.Entry<String, Map<String, String>> entry : chemical.getProperties().entrySet()) {
                Map<String, AttributeValue> properties = new HashMap<String, AttributeValue>();

                for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                    properties.put(entry2.getKey(), new AttributeValue(entry2.getValue()));
                }

                item.put(entry.getKey(), new AttributeValue().withM(properties));
            }

            item.put(Chemical.IMAGE_URL, new AttributeValue(chemical.getImageURL()));
        } catch (Exception e) {
            logger.error("Error occurred while converting the chemical: " + chemical.getName(), e);
        }

        return item;
    }

    private Chemical convertItemToChemical(final Map<String, AttributeValue> item) {

        if (item == null) {
            logger.error("A null object was passed as a parameter to be converted to a Chemical object");
            return null;
        }

        final Chemical chemical = new Chemical();
        chemical.setMatch(true);
        chemical.setName(item.get(Chemical.NAME).getS());

        try {
            final FireDiamond fireDiamond = new FireDiamond(
                    Integer.parseInt(item.get(FireDiamond.FLAMMABILITY).getN()),
                    Integer.parseInt(item.get(FireDiamond.HEALTH).getN()),
                    Integer.parseInt(item.get(FireDiamond.INSTABILITY).getN()),
                    item.get(FireDiamond.NOTICE).getS());

            chemical.setFireDiamond(fireDiamond);

        } catch (Exception e) {
            logger.error("Error occurred while creating the fire Diamond", e);
        }

        final Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

        try {

            // Add properties to chemical
            for (Map.Entry<String, Map<String, String>> entry : chemical.getProperties().entrySet()) {

                Map<String, String> subProperties = new HashMap<String, String>();

                for (Map.Entry<String, AttributeValue> entry2 : item.get(entry.getKey()).getM().entrySet()) {
                    subProperties.put(entry2.getKey(), entry2.getValue().getS());
                }

                properties.put(entry.getKey(), subProperties);
            }

            chemical.setProperties(properties);

        } catch (Exception e) {
            logger.error("Error occured while converting a DynamoDB Item to a chemical object", e);
        }

        return chemical;
    }

    // This is a temporary method for updating the chemical row that contains
    // all of the chemical names
    public void updateChemicalNames() {

        // Query for all elements in the Database
        final List<String> chemicalNames = new ArrayList<String>();
        final ScanRequest scanRequest = new ScanRequest()
            .withTableName(CHEMICALS_TABLE_NAME);


        try {
            final ScanResult result = amazonDynamoDBClient.scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {
                chemicalNames.add(item.get(Chemical.NAME).getS());
            }

        } catch (AmazonServiceException e) {
            logger.error("Error occurred while trying to get all the chemical names for the table: " +
                    CHEMICALS_TABLE_NAME);
        }
 
        logger.info("Updating All Chemicals row");
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put("Name", new AttributeValue("All"));
        item.put("Chemicals", new AttributeValue(chemicalNames));
        
        final PutItemRequest putItemRequest = new PutItemRequest()
            .withTableName(CHEMICALS_TABLE_NAME)
            .withItem(item);

        try {
            amazonDynamoDBClient.putItem(putItemRequest);
        } catch (AmazonServiceException e) {
            logger.error("Error occurred while trying to add chemical: " + " to table: " + CHEMICALS_TABLE_NAME, e);
        }
    }
}

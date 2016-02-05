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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public class ChemicalDAO {

    private static volatile ChemicalDAO instance;

    private static final Logger logger = Logger.getLogger(ChemicalDAO.class);

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    private DynamoDBMapper mapper;
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

        mapper = new DynamoDBMapper(amazonDynamoDBClient);
    }

    public Chemical getChemical(final String name) {

        final Chemical partitionKeyKeyValues = new Chemical();
        partitionKeyKeyValues.setName(name);

        final DynamoDBQueryExpression<Chemical> queryExpression = new DynamoDBQueryExpression<Chemical>()
            .withHashKeyValues(partitionKeyKeyValues);

        final List<Chemical> itemList = mapper.query(Chemical.class, queryExpression);

        if (itemList.isEmpty()) {
            return new Chemical().withMatch(false);
        }
        return itemList.get(0); // return first match
    }

    public List<Chemical> searchPartialChemicalName(final List<String> partialNames) {

        final Map<String, String> expressionAttributeNames =
            new HashMap<String, String>();

        expressionAttributeNames.put("#name", "Name");

        final Map<String, AttributeValue> expressionAttributeValues = 
            new HashMap<String, AttributeValue>();

        int count = 0;
        String filterExpression = "";
        for (String partialName : partialNames) {
            if (count != 0) {
                filterExpression += " or ";
            }
            filterExpression += "contains (#name, :val" + count + ")";
            expressionAttributeValues.put(":val" + count, new AttributeValue().withS(partialName));
            count += 1;
        }

        final DynamoDBScanExpression scanRequest = new DynamoDBScanExpression()
            .withFilterExpression(filterExpression)
            .withExpressionAttributeNames(expressionAttributeNames)
            .withExpressionAttributeValues(expressionAttributeValues);


        return mapper.scan(Chemical.class, scanRequest);
    }

    public List<Chemical> searchPartialChemicalName(final String partialName) {
        
        final Map<String, AttributeValue> expressionAttributeValues = 
            new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(partialName));

        final Map<String, String> expressionAttributeNames =
            new HashMap<String, String>();

        expressionAttributeNames.put("#name", "Name");
                
        final DynamoDBScanExpression scanRequest = new DynamoDBScanExpression()
            .withFilterExpression("contains (#name, :val)")
            .withExpressionAttributeNames(expressionAttributeNames)
            .withExpressionAttributeValues(expressionAttributeValues);

        return mapper.scan(Chemical.class, scanRequest);
    }

    public void updateChemical(final Chemical chemical) {
        addChemical(chemical);
    }

    public void deleteChemical(final Chemical chemical) {
        mapper.delete(chemical);
    }

    public void addChemical(final Chemical chemical) {
        mapper.save(chemical);
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

        final Map<String, String> expressionAttributeNames =
            new HashMap<String, String>();

        expressionAttributeNames.put("#name", "Name");

        final Map<String, AttributeValue> expressionAttributeValues = 
            new HashMap<String, AttributeValue>();

        int count = 0;
        String filterExpression = "";
        for (String name : names) {
            if (count != 0) {
                filterExpression += " or ";
            }
            filterExpression += "#name = :val" + count;
            expressionAttributeValues.put(":val" + count, new AttributeValue().withS(name));
            count += 1;
        }

        final DynamoDBScanExpression scanRequest = new DynamoDBScanExpression()
            .withFilterExpression(filterExpression)
            .withExpressionAttributeNames(expressionAttributeNames)
            .withExpressionAttributeValues(expressionAttributeValues);

        return mapper.scan(Chemical.class, scanRequest);
    }
}

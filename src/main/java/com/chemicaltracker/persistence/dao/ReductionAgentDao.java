package com.chemicaltracker.persistence.dao;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public class ReductionAgentDao {

    private static volatile ReductionAgentDao instance;

    private static final Logger logger = Logger.getLogger(ReductionAgentDao.class);

    private static final String CHEMICALS_TABLE_NAME = "ReductionAgents";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private ReductionAgentDao() {

        try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occured while initializing DB Connection", e);
        }
    }

    public static ReductionAgentDao getInstance() {

        if (instance == null) {
            synchronized (ReductionAgentDao.class) {
                if (instance == null) {
                    instance = new ReductionAgentDao();
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

    public List<String> getAllAgents() {

        final List<String> chemicalNames = new ArrayList<>();
        final ScanRequest scanRequest = new ScanRequest()
                .withTableName(CHEMICALS_TABLE_NAME);

        try {
            final ScanResult result = amazonDynamoDBClient.scan(scanRequest);

            for (Map<String, AttributeValue> item : result.getItems()) {
                chemicalNames.add(item.get("Name").getS());
            }

        } catch (Exception e) {
            logger.error("Error occurred while scanning for all chemical agents in table: " + CHEMICALS_TABLE_NAME, e);
        }

        return chemicalNames;
    }

    public boolean isAgent(final String name) {

        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS(name));

        final GetItemRequest request = new GetItemRequest()
                .withTableName(CHEMICALS_TABLE_NAME)
                .withKey(key);

        try {
            final GetItemResult result = amazonDynamoDBClient.getItem(request);

            if (result.getItem() == null) {
                return false;
            }

        } catch (Exception e) {
            logger.error("Error occurred while getting chemical agent: " + name + " from table: " + CHEMICALS_TABLE_NAME);
        }

        return true;
    }

    public void deleteAgent(final String name) {

        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(CHEMICALS_TABLE_INDEX, new AttributeValue().withS(name));

        final DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
                .withTableName(CHEMICALS_TABLE_NAME)
                .withKey(key);

        try {
            amazonDynamoDBClient.deleteItem(deleteItemRequest);
        } catch (AmazonServiceException e) {
            logger.error("Error occured while trying to delete chemical agent: " +
                    name + " from table: " + CHEMICALS_TABLE_NAME);
        }
    }

    public void addAgent(final String name) {

        final Map<String, AttributeValue> item = new HashMap<>();
        item.put("Name", new AttributeValue(name));

        final PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName(CHEMICALS_TABLE_NAME)
                .withItem(item);

        try {
            amazonDynamoDBClient.putItem(putItemRequest);
        } catch (AmazonServiceException e) {
            logger.error("Error occurred while trying to add chemical agent: " +
                    name + " to table: " + CHEMICALS_TABLE_NAME, e);
        }
    }

    public boolean containsAgent(final List<String> names) {
        for (String name : names) {
            if (isAgent(name)) {
                return true;
            }
        }
        return false;
    }
}

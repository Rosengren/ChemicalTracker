package com.chemicaltracker.persistence.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public class OxidizingAgentDao {

    private static volatile OxidizingAgentDao instance;

    private static final Logger logger = Logger.getLogger(OxidizingAgentDao.class);

    private static final String CHEMICALS_TABLE_NAME = "OxidizingAgents";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private OxidizingAgentDao() {

        try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occurred while initializing DB Connection", e);
        }
    }

    public static OxidizingAgentDao getInstance() {

        if (instance == null) {
            synchronized (OxidizingAgentDao.class) {
                if (instance == null) {
                    instance = new OxidizingAgentDao();
                }
            }
        }

        return instance;
    }

    public void initializeDBConnection() throws AmazonClientException {

        AWSCredentials credentials;

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

        final List<String> chemicalNames = new ArrayList<String>();
        final ScanRequest scanRequest = new ScanRequest()
                .withTableName(CHEMICALS_TABLE_NAME);

        try {
            final ScanResult result = amazonDynamoDBClient.scan(scanRequest);

            chemicalNames.addAll(result.getItems().stream().map(item ->
                    item.get(CHEMICALS_TABLE_INDEX).getS()).collect(Collectors.toList()));

        } catch (Exception e) {
            logger.error("Error occurred while scanning for all chemical agents in table: " + CHEMICALS_TABLE_NAME, e);
        }

        return chemicalNames;
    }

    public boolean containsAgent(final List<String> names) {

        final List<String> agents = getAllAgents();
        for (String name : names) {
            if (agents.contains(name)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfAgents(final List<String> names) {

        int count = 0;
        final List<String> agents = getAllAgents();
        for (String name : names) {
            if (agents.contains(name)) {
                count++;
            }
        }
        return count;
    }
}

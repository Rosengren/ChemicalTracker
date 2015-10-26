package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Login;

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

public class UsersDataAccessDynamoDB implements UsersDataAccessObject {

    private AmazonDynamoDBClient dynamoDB;

    public UsersDataAccessDynamoDB() {
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
    public boolean validateLogin(Login login) {

        //final Map<String, 
        return false;
    }
}

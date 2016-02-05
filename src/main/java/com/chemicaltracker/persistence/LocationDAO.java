package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Location;

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
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
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
import com.amazonaws.services.dynamodbv2.document.ItemCollection;

import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class LocationDAO extends DynamoDBDAO<Location>  {

    private static volatile LocationDAO instance;

    public static LocationDAO getInstance() {

        if (instance == null) {
            synchronized (LocationDAO.class) {
                if (instance == null) {
                    instance = new LocationDAO();
                }
            }
        }

        return instance;
    }

    public List<Location> findAll(final String hash) {

       Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(hash));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("Username = :val1")
            .withExpressionAttributeValues(eav);
        
        return mapper.scan(Location.class, scanExpression);
    }

    // public LocationDAO(final String tableName, final String tableHashKey,
    //         final String tableRangeKey, final String storedItemTitle) {

    //     this.tableName = tableName;
    //     this.tableHashKey = tableHashKey;
    //     this.tableRangeKey = tableRangeKey;
    //     this.storedItemTitle = storedItemTitle;

    //     try {
    //         initializeDBConnection();
    //     } catch (Exception e) {
    //         System.out.println("Error occured while initializing DB Connection");
    //     }
    // }

    @Override
    public Class getObjectClass() {
        return Location.class;
    }

    // public List<Location> getAllStoragesForUser(final String username) {

    //     final List<Location> storages = new ArrayList<Storage>();
    //     final Map<String, AttributeValue> attributes = new HashMap<String, AttributeValue>();
    //     attributes.put(":hashval", new AttributeValue(username));

    //     final QueryRequest request = new QueryRequest()
    //         .withTableName(tableName)
    //         .withKeyConditionExpression(tableHashKey + " = :hashval")
    //         .withExpressionAttributeValues(attributes);

    //     try {
    //         final QueryResult result = amazonDynamoDBClient.query(request);
    //         for (Map<String, AttributeValue> item : result.getItems()) {
    //             storages.add(convertItemToStorage(item));
    //         }
    //     } catch (Exception e) {
    //         logger.error("Error occurred while querying the database for all the storages for user: " + username, e);
    //     }

    //     return storages;
    // }

    // public Storage getStorage(final String username, final String storageID) {
    //     final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
    //     key.put(tableHashKey, new AttributeValue().withS(username));
    //     key.put(tableRangeKey, new AttributeValue().withS(storageID));

    //     final GetItemRequest request = new GetItemRequest()
    //         .withTableName(tableName)
    //         .withKey(key);

    //     Storage storage = new Storage();

    //     try {
    //         final GetItemResult result = amazonDynamoDBClient.getItem(request);
    //         storage = convertItemToStorage(result.getItem());
    //     } catch (Exception e) {
    //         logger.error("Error occurred while getting storage: " + storageID + " for user: " + username, e);
    //     }

    //     return storage;
    // }
}
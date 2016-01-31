package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Storage;

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

import org.apache.log4j.Logger;

public class StorageDAO {

    private static final Logger logger = Logger.getLogger(StorageDAO.class);

    private String tableName;
    private String tableHashKey;
    private String tableRangeKey;
    private String storedItemTitle;

    private AmazonDynamoDBClient amazonDynamoDBClient;

    public StorageDAO(final String tableName, final String tableHashKey,
            final String tableRangeKey, final String storedItemTitle) {

        this.tableName = tableName;
        this.tableHashKey = tableHashKey;
        this.tableRangeKey = tableRangeKey;
        this.storedItemTitle = storedItemTitle;

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

        try {
            amazonDynamoDBClient = new AmazonDynamoDBClient(credentials);
        } catch (Exception e) {
            throw new AmazonClientException("The provided credentials for the storage DB were not valid", e);
        }

        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        amazonDynamoDBClient.setRegion(usWest2);
    }

    public List<Storage> getAllStoragesForUser(final String username) {

        final List<Storage> storages = new ArrayList<Storage>();
        final Map<String, AttributeValue> attributes = new HashMap<String, AttributeValue>();
        attributes.put(":hashval", new AttributeValue(username));

        final QueryRequest request = new QueryRequest()
            .withTableName(tableName)
            .withKeyConditionExpression(tableHashKey + " = :hashval")
            .withExpressionAttributeValues(attributes);

        try {
            final QueryResult result = amazonDynamoDBClient.query(request);
            for (Map<String, AttributeValue> item : result.getItems()) {
                storages.add(convertItemToStorage(item));
            }
        } catch (Exception e) {
            logger.error("Error occurred while querying the database for all the storages for user: " + username, e);
        }

        return storages;
    }

    public Storage getStorage(final String username, final String storageID) {
        final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(tableHashKey, new AttributeValue().withS(username));
        key.put(tableRangeKey, new AttributeValue().withS(storageID));

        final GetItemRequest request = new GetItemRequest()
            .withTableName(tableName)
            .withKey(key);

        Storage storage = new Storage();

        try {
            final GetItemResult result = amazonDynamoDBClient.getItem(request);
            storage = convertItemToStorage(result.getItem());
        } catch (Exception e) {
            logger.error("Error occurred while getting storage: " + storageID + " for user: " + username, e);
        }

        return storage;
    }

    public void updateStorage(final Storage storage) {
        addStorage(storage);
    }

    public void deleteStorage(final String username, final String storageID) {
        final Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(tableHashKey, new AttributeValue().withS(username));
        key.put(tableRangeKey, new AttributeValue().withS(storageID));

        final DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
            .withTableName(tableName)
            .withKey(key);

        try {
            amazonDynamoDBClient.deleteItem(deleteItemRequest);
        } catch (Exception e) {
            logger.error("Error occurred while deleting item: " + storageID + " for user: " + username, e);
        }
    }

    public void addStorage(final Storage storage) {
        logger.info("ADDING STORAGE");
        final Map<String, AttributeValue> item = convertStorageToItem(storage);
        final PutItemRequest putItemRequest = new PutItemRequest()
            .withTableName(tableName)
            .withItem(item);

        logger.info("ADDING: " + item.toString());
        try {
            amazonDynamoDBClient.putItem(putItemRequest);
        } catch (Exception e) {
            logger.error("Error occurred while adding storage: " + storage.getName());
        }
    }

    public List<Storage> batchGetStorages(final String username, final List<String> storageIDs) {
        final List<Storage> storages = new ArrayList<Storage>();

        for (String id : storageIDs) {
            storages.add(getStorage(username, id));
        }

        return storages;
    }

    private Map<String, AttributeValue> convertStorageToItem(final Storage storage) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put(tableHashKey, new AttributeValue(storage.getUsername()));
        item.put(Storage.NAME, new AttributeValue(storage.getName()));
        item.put(tableRangeKey, new AttributeValue(storage.getID()));
        item.put(Storage.DESCRIPTION, new AttributeValue(storage.getDescription()));

        List<String> tagNames = storage.getTagNames(); // DB elements cannot be empty
        if (tagNames.isEmpty()) {
            tagNames.add(" ");
        }

        item.put(Storage.TAGS, new AttributeValue(tagNames));
        item.put(Storage.IMAGE_URL, new AttributeValue(storage.getImageURL()));

        final Map<String, AttributeValue> storedItems = new HashMap<String, AttributeValue>();
        for (Map.Entry<String, String> storedItem : storage.getStoredItemsSet()) {
            storedItems.put(storedItem.getKey(), new AttributeValue(storedItem.getValue()));
        }

        item.put(storedItemTitle, new AttributeValue().withM(storedItems));
        logger.info("ITEM: " + item.toString());
        return item;
    }

    private Storage convertItemToStorage(final Map<String, AttributeValue> item) {

        if (item == null) {
            logger.error("Error occurred while converting an Item to Storage object. item was null");
            return null;
        }

        final Map<String, String> storedItems = new HashMap<String, String>();
        for (Map.Entry<String, AttributeValue> storedItem : item.get(storedItemTitle).getM().entrySet()) {
            storedItems.put(storedItem.getKey(), storedItem.getValue().getS());
        }

        return new Storage(
                item.get(tableHashKey).getS(),
                item.get(Storage.NAME).getS(),
                item.get(tableRangeKey).getS(),
                item.get(Storage.DESCRIPTION).getS(),
                item.get(Storage.TAGS).getSS(),
                storedItems,
                item.get(Storage.IMAGE_URL).getS());
    }
}

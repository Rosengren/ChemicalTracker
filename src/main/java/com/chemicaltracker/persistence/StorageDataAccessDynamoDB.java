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

public class StorageDataAccessDynamoDB implements StorageDataAccessObject {

    private String tableName;
    private String tableHashKey;
    private String tableRangeKey;
    private String storedItemTitle;

    private AmazonDynamoDBClient dynamoDB;

    public StorageDataAccessDynamoDB(final String tableName, final String tableHashKey,
            final String tableRangeKey, final String storedItemTitle) throws Exception {

        this.tableName = tableName;
        this.tableHashKey = tableHashKey;
        this.tableRangeKey = tableRangeKey;
        this.storedItemTitle = storedItemTitle;

        try {
            initializeDBConnection();
        } catch (Exception e) {
            System.out.println("Error occured while initializing DB Connection");
            throw e;
        }
    }

    public void initializeDBConnection() throws Exception {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
            //credentials = new DefaultAWSCredentialsProviderChain().getCredentials();
            //System.out.println("DEBUG: " + credentials.getAWSAccessKeyId() + "   " + credentials.getAWSSecretKey());
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        dynamoDB = new AmazonDynamoDBClient(credentials);
        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    @Override
    public List<Storage> getAllStoragesForUser(final String username) {
        // TODO: may need a more efficient way of converting these values instead of looping
        final Map<String, AttributeValue> attributes = new HashMap<String, AttributeValue>();
        attributes.put(":hashval", new AttributeValue(username));

        final QueryRequest request = new QueryRequest()
            .withTableName(tableName)
            .withKeyConditionExpression(tableHashKey + " = :hashval")
            .withExpressionAttributeValues(attributes);

        final QueryResult result = dynamoDB.query(request);
        final List<Storage> storages = new ArrayList<Storage>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            storages.add(convertItemToStorage(item));
        }

        return storages;
    }

    @Override
    public Storage getStorage(final String username, final String storageID) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(tableHashKey, new AttributeValue().withS(username));
        item.put(tableRangeKey, new AttributeValue().withS(storageID));

        final GetItemRequest request = new GetItemRequest(tableName, item);
        final GetItemResult result = dynamoDB.getItem(request);

        return convertItemToStorage(result.getItem());
    }

    @Override
    public void updateStorage(final Storage storage) {
        addStorage(storage);
    }

    @Override
    public void deleteStorage(final String username, final String storageID) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(tableHashKey, new AttributeValue().withS(username));
        item.put(tableRangeKey, new AttributeValue().withS(storageID));

        final DeleteItemRequest deleteItemRequest = new DeleteItemRequest(tableName, item);
        final DeleteItemResult result = dynamoDB.deleteItem(deleteItemRequest);
    }

    @Override
    public void addStorage(final Storage storage) {
        // TODO: add check that chemical was correctly added
        final Map<String, AttributeValue> item = convertStorageToItem(storage);
        final PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        final PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
    }

    @Override
    public List<Storage> batchGetStorages(final String username, final List<String> storageIDs) {
        final List<Storage> storages = new ArrayList<Storage>();

        // TODO: create more efficient batch method
        for (String id : storageIDs) {
            storages.add(getStorage(username, id));
        }

        return storages;
    }

    // TODO: move this into a new class which handles the conversion
    // TODO: replace Map<> with Item()
    private Map<String, AttributeValue> convertStorageToItem(final Storage storage) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put(tableHashKey, new AttributeValue(storage.getUsername()));
        item.put("Name", new AttributeValue(storage.getName()));
        item.put(tableRangeKey, new AttributeValue(storage.getID()));
        item.put("Description", new AttributeValue(storage.getDescription()));
        item.put("Image URL", new AttributeValue(storage.getImageURL()));

        final Map<String, AttributeValue> storedItems = new HashMap<String, AttributeValue>();
        for (Map.Entry<String, String> storedItem : storage.getStoredItemsSet()) {
            storedItems.put(storedItem.getKey(), new AttributeValue(storedItem.getValue()));
        }

        item.put(storedItemTitle, new AttributeValue().withM(storedItems));
        return item;
    }

    private Storage convertItemToStorage(final Map<String, AttributeValue> item) {

        // TODO: add Exception
        if (item == null) {
            // throw exception
            return null;
        }

        final Map<String, String> storedItems = new HashMap<String, String>();
        for (Map.Entry<String, AttributeValue> storedItem : item.get(storedItemTitle).getM().entrySet()) {
            storedItems.put(storedItem.getKey(), storedItem.getValue().getS());
        }

        return new Storage(
                item.get(tableHashKey).getS(),
                item.get("Name").getS(),
                item.get(tableRangeKey).getS(),
                item.get("Description").getS(),
                storedItems,
                item.get("Image URL").getS());
    }
}

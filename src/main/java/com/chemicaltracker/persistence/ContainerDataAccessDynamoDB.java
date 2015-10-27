package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Container;
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
//import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

//import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;

public class ContainerDataAccessDynamoDB implements ContainerDataAccessObject {

    private static final String CONTAINERS_TABLE_NAME = "Containers";
    private static final String CONTAINERS_TABLE_HASH_KEY = "Username";
    private static final String CONTAINERS_TABLE_RANGE_KEY = "Container Name";

    private AmazonDynamoDBClient dynamoDB;

    public ContainerDataAccessDynamoDB() {
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
    public List<Container> getAllContainersForUser(final String username) {
        // TODO: may need a more efficient way of converting these values instead of looping
        //final QuerySpec spec = new QuerySpec()
            //.withHashKey(CONTAINERS_TABLE_HASH_KEY, username);
        //final Condition condition = new Condition()
            //.withAttributeValueList(new AttributeValue(username));

        final QueryRequest request = new QueryRequest()
            .withKeyConditionExpression(CONTAINERS_TABLE_HASH_KEY + " = " + username);

        request.setTableName(CONTAINERS_TABLE_NAME);
        //final ItemCollection<QueryOutcome> items = dynamoDB.query(request);
        final QueryResult result = dynamoDB.query(request);

        final List<Container> containers = new ArrayList<Container>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            containers.add(convertItemToContainer(item));
        }

        return containers;
    }

    @Override
    public Container getContainer(final String username, final String containerName) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(CONTAINERS_TABLE_HASH_KEY, new AttributeValue().withS(username));
        item.put(CONTAINERS_TABLE_RANGE_KEY, new AttributeValue().withS(containerName));

        final GetItemRequest request = new GetItemRequest(CONTAINERS_TABLE_NAME, item);
        final GetItemResult result = dynamoDB.getItem(request);

        return convertItemToContainer(result.getItem());
    }

    @Override
    public void updateContainer(final Container container) {
        addContainer(container);
    }

    @Override
    public void deleteContainer(final Container container) {
        // TODO: implement this
        //Map<String, AttributeValue> item = convertContainerToItem(container);
        //DeleteItemRequest deleteItemRequest = new DeleteItemRequest(CONTAINERS_TABLE_NAME, item);
        //DeleteItemResult result = dynamoDB.deleteItem(deleteItemRequest);
    }

    @Override
    public void addContainer(final Container container) {
        // TODO: add check that chemical was correctly added
        final Map<String, AttributeValue> item = convertContainerToItem(container);
        final PutItemRequest putItemRequest = new PutItemRequest(CONTAINERS_TABLE_NAME, item);
        final PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
    }

    @Override
    public List<Container> batchGetContainers(final String username, final List<String> containerNames) {
        final List<Container> containers = new ArrayList<Container>();

        // TODO: create more effecient batch method
        for (String name : containerNames) {
            containers.add(getContainer(username, name));
        }

        return containers;
    }

    // TODO: move this into a new class which handles the conversion
    // TODO: replace Map<> with Item()
    private Map<String, AttributeValue> convertContainerToItem(final Container container) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        item.put(CONTAINERS_TABLE_HASH_KEY, new AttributeValue(container.getUsername()));
        item.put(CONTAINERS_TABLE_RANGE_KEY, new AttributeValue(container.getContainerName()));
        item.put("description", new AttributeValue(container.getDescription()));
        item.put("chemicalNames", new AttributeValue(container.getChemicalNames()));

        return item;
    }

    private Container convertItemToContainer(final Map<String, AttributeValue> item) {

        // TODO: add Exception
        if (item == null) {
            // throw exception
            return null;
        }

        return new Container(
                item.get("username").getS(),
                item.get("containerName").getS(),
                item.get("description").getS(),
                item.get("chemicalNames").getSS());
    }
}

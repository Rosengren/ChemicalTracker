package com.chemicaltracker.persistence;

import com.chemicaltracker.model.storage.Chemical;

import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class ChemicalDAO extends DynamoDBDAO<Chemical> {

    private static volatile ChemicalDAO instance;

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

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

    @Override
    public Class getObjectClass() {
        return Chemical.class;
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

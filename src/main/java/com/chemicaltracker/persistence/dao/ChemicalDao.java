package com.chemicaltracker.persistence.dao;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.chemicaltracker.persistence.model.Chemical;

public class ChemicalDao extends DynamoDBDao<Chemical> {

    private static volatile ChemicalDao instance;

    private static final String CHEMICALS_TABLE_NAME = "Chemicals";
    private static final String CHEMICALS_TABLE_INDEX = "Name";

    public static ChemicalDao getInstance() {

        if (instance == null) {
            synchronized (ChemicalDao.class) {
                if (instance == null) {
                    instance = new ChemicalDao();
                }
            }
        }

        return instance;
    }

    public List<Chemical> searchPartialChemicalName(final List<String> partialNames) {

        final Map<String, String> expressionAttributeNames = new HashMap<>();

        expressionAttributeNames.put("#name", "Name");

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

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

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(partialName));

        final Map<String, String> expressionAttributeNames = new HashMap<>();

        expressionAttributeNames.put("#name", "Name");

        final DynamoDBScanExpression scanRequest = new DynamoDBScanExpression()
                .withFilterExpression("contains (#name, :val)")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        return mapper.scan(Chemical.class, scanRequest);
    }

    public List<String> getAllChemicalNames() {

        final Map<String, AttributeValue> key = new HashMap<>();
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

        return new ArrayList<>();
    }

    public List<Chemical> findByNames(final List<String> names) {

        if (names == null || names.isEmpty()) {
            return new ArrayList<>();
        }

        final Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#name", "Name");

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

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

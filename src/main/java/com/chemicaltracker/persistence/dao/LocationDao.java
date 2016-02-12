package com.chemicaltracker.persistence.dao;

import com.chemicaltracker.persistence.model.Location;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class LocationDao extends DynamoDBDao<Location>  {

    private static volatile LocationDao instance;

    public static LocationDao getInstance() {

        if (instance == null) {
            synchronized (LocationDao.class) {
                if (instance == null) {
                    instance = new LocationDao();
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
}
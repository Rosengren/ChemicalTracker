package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Location;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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

    @Override
    public Class getObjectClass() {
        return Location.class;
    }
}
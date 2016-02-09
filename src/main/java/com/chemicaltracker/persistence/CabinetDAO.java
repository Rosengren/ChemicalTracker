package com.chemicaltracker.persistence;

import com.chemicaltracker.model.storage.Cabinet;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class CabinetDAO extends DynamoDBDAO<Cabinet>  {

    private static volatile CabinetDAO instance;

    public static CabinetDAO getInstance() {

        if (instance == null) {
            synchronized (CabinetDAO.class) {
                if (instance == null) {
                    instance = new CabinetDAO();
                }
            }
        }

        return instance;
    }

    public List<Cabinet> findAll(final String hash) {

       Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(hash));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("Username = :val1")
            .withExpressionAttributeValues(eav);
        
        return mapper.scan(Cabinet.class, scanExpression);
    }

    public List<Cabinet> findAllCabinets(final String hash, final List<String> cabinetIDs) {

        final List<Cabinet> cabinets = new ArrayList<Cabinet>();

        for (String id : cabinetIDs) {
            cabinets.add(find(hash, id));
        }

        return cabinets;
    }
}
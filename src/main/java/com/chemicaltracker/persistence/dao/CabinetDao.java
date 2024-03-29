package com.chemicaltracker.persistence.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.chemicaltracker.persistence.model.Cabinet;

public class CabinetDao extends DynamoDBDao<Cabinet>  {

    private static volatile CabinetDao instance;

    public static CabinetDao getInstance() {

        if (instance == null) {
            synchronized (CabinetDao.class) {
                if (instance == null) {
                    instance = new CabinetDao();
                }
            }
        }

        return instance;
    }

    public List<Cabinet> findAllByIds(final String hash, final List<String> cabinetIDs) {

        final List<Cabinet> cabinets = new ArrayList<>();

        for (String id : cabinetIDs) {
            cabinets.add(find(hash, id));
        }

        return cabinets;
    }
}
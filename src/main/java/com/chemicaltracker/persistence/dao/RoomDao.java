package com.chemicaltracker.persistence.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chemicaltracker.persistence.model.Room;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RoomDao extends DynamoDBDao<Room>  {

    private static volatile RoomDao instance;

    public static RoomDao getInstance() {

        if (instance == null) {
            synchronized (RoomDao.class) {
                if (instance == null) {
                    instance = new RoomDao();
                }
            }
        }

        return instance;
    }

    public List<Room> findAll(final String hash) {

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(hash));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("Username = :val1") // might be able to get the key name somehow
                .withExpressionAttributeValues(eav);

        return mapper.scan(Room.class, scanExpression);
    }

    // TODO: make more efficient -- Might be able to make this generic
    public List<Room> findAllByIds(final String hash, final List<String> roomIDs) {

        final List<Room> rooms = new ArrayList<Room>();

        for (String id : roomIDs) {
            rooms.add(find(hash, id));
        }

        return rooms;
    }
}
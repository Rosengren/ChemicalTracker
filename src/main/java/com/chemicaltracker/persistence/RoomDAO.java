package com.chemicaltracker.persistence;

import com.chemicaltracker.model.storage.Room;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class RoomDAO extends DynamoDBDAO<Room>  {

    private static volatile RoomDAO instance;

    public static RoomDAO getInstance() {

        if (instance == null) {
            synchronized (RoomDAO.class) {
                if (instance == null) {
                    instance = new RoomDAO();
                }
            }
        }

        return instance;
    }

    public List<Room> findAll(final String hash) {

       Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(hash));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("Username = :val1") // might be able to get the key somehow
            .withExpressionAttributeValues(eav);
        
        return mapper.scan(Room.class, scanExpression);
    }

    public List<Room> findAllRooms(final String hash, final List<String> roomIDs) {

        final List<Room> rooms = new ArrayList<Room>();

        for (String id : roomIDs) {
            rooms.add(find(hash, id));
        }

        return rooms;
    } 

    @Override
    public Class getObjectClass() {
        return Room.class;
    }
}
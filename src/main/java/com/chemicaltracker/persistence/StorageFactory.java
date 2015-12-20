package com.chemicaltracker.persistence;

import org.apache.log4j.Logger;

public class StorageFactory {

    private static volatile StorageDataAccessDynamoDB cabinetsDB;
    private static volatile StorageDataAccessDynamoDB roomsDB;
    private static volatile StorageDataAccessDynamoDB locationsDB;

    private static final Logger logger = Logger.getLogger(StorageFactory.class);

    public static StorageDataAccessObject getStorage(String storageName) {

        if (storageName == null || storageName == "") {
            logger.error("Error occurred while creating storage. storageName was null or empty");
            return null;
        }

        if (storageName.equalsIgnoreCase("CABINETS")) {

            if (cabinetsDB == null) {
                synchronized (StorageDataAccessDynamoDB.class) {
                    if (cabinetsDB == null) {
                        cabinetsDB = new StorageDataAccessDynamoDB(
                                "Cabinets", "Username", "Cabinet ID", "Chemical Names");
                    }
                }
            }

            return cabinetsDB;

        } else if (storageName.equalsIgnoreCase("ROOMS")) {

            if (roomsDB == null) {
                synchronized (StorageDataAccessDynamoDB.class) {
                    if (roomsDB == null) {
                        roomsDB = new StorageDataAccessDynamoDB(
                                "Rooms", "Username", "Room ID", "Cabinet Names");
                    }
                }
            }

            return roomsDB;

        } else if (storageName.equalsIgnoreCase("LOCATIONS")) {

            if (locationsDB == null) {
                synchronized (StorageDataAccessDynamoDB.class) {
                    if (locationsDB == null) {
                        locationsDB = new StorageDataAccessDynamoDB(
                                "Locations", "Username", "Location ID", "Room Names");
                    }
                }
            }

            return locationsDB;
        }

        return null;
    }

}

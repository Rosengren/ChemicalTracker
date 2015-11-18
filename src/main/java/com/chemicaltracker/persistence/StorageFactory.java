package com.chemicaltracker.persistence;

import org.apache.log4j.Logger;

public class StorageFactory {

    private static final Logger logger = Logger.getLogger(StorageFactory.class);

    public static StorageDataAccessObject getStorage(String storageName) {

        if (storageName == null || storageName == "") {
            logger.error("Error occurred while creating storage. storageName was null or empty");
            return null;
        }

        if (storageName.equalsIgnoreCase("CABINETS")) {

            return new StorageDataAccessDynamoDB("Cabinets", "Username", "Cabinet ID", "Chemical Names");

        } else if (storageName.equalsIgnoreCase("ROOMS")) {

            return new StorageDataAccessDynamoDB("Rooms", "Username", "Room ID", "Cabinet Names");

        } else if (storageName.equalsIgnoreCase("LOCATIONS")) {

            return new StorageDataAccessDynamoDB("Locations", "Username", "Location ID", "Room Names");
        }

        return null;
    }

}

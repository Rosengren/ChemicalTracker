package com.chemicaltracker.persistence;

public class StorageFactory {

    public static StorageDataAccessObject getStorage(String storageName) {

        if (storageName == null) {
            return null;
        }

        try {
            if (storageName.equalsIgnoreCase("CABINETS")) {
                return new StorageDataAccessDynamoDB("Cabinets", "Username", "Cabinet ID", "Chemical Names");
            } else if (storageName.equalsIgnoreCase("ROOMS")) {
                return new StorageDataAccessDynamoDB("Rooms", "Username", "Room ID", "Cabinet Names");
            } else if (storageName.equalsIgnoreCase("LOCATIONS")) {
                return new StorageDataAccessDynamoDB("Locations", "Username", "Location ID", "Room Names");
            }
        } catch (Exception e) {

        }

        return null;
    }

}

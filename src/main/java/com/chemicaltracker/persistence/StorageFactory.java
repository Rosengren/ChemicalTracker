package com.chemicaltracker.persistence;

public class StorageFactory {

    public static StorageDataAccessObject getStorage(String storageName) {

        if (storageName == null) {
            return null;
        }

        if (storageName.equalsIgnoreCase("CABINETS")) {
            return new StorageDataAccessDynamoDB("Cabinets", "Username", "Cabinet Name", "Chemical Names");
        } else if (storageName.equalsIgnoreCase("ROOMS")) {
            return new StorageDataAccessDynamoDB("Rooms", "Username", "Room Name", "Cabinet Names");
        } else if (storageName.equalsIgnoreCase("LOCATIONS")) {
            return new StorageDataAccessDynamoDB("Locations", "Username", "Location Name", "Room Names");
        }

        return null;
    }

}

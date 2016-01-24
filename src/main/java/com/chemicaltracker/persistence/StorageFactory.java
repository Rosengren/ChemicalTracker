package com.chemicaltracker.persistence;

import org.apache.log4j.Logger;

public class StorageFactory {

    private static volatile StorageDAO cabinetsDB;
    private static volatile StorageDAO roomsDB;
    private static volatile StorageDAO locationsDB;

    private static final Logger logger = Logger.getLogger(StorageFactory.class);

    public static StorageDAO getStorage(String storageName) {

        if (storageName == null || storageName == "") {
            logger.error("Error occurred while creating storage. storageName was null or empty");
            return null;
        }

        if (storageName.equalsIgnoreCase("CABINETS")) {

            if (cabinetsDB == null) {
                synchronized (StorageDAO.class) {
                    if (cabinetsDB == null) {
                        cabinetsDB = new StorageDAO(
                                "Cabinets", "Username", "Cabinet ID", "Chemical Names");
                    }
                }
            }

            return cabinetsDB;

        } else if (storageName.equalsIgnoreCase("ROOMS")) {

            if (roomsDB == null) {
                synchronized (StorageDAO.class) {
                    if (roomsDB == null) {
                        roomsDB = new StorageDAO(
                                "Rooms", "Username", "Room ID", "Cabinet Names");
                    }
                }
            }

            return roomsDB;

        } else if (storageName.equalsIgnoreCase("LOCATIONS")) {

            if (locationsDB == null) {
                synchronized (StorageDAO.class) {
                    if (locationsDB == null) {
                        locationsDB = new StorageDAO(
                                "Locations", "Username", "Location ID", "Room Names");
                    }
                }
            }

            return locationsDB;
        }

        return null;
    }

}

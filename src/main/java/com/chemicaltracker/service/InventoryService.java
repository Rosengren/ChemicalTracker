package com.chemicaltracker.service;


import com.chemicaltracker.model.Comparison;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;

import java.util.List;

public interface InventoryService {

    Location getLocation(String username, String locationName);

    Room getRoom(String username, String locationName, String roomName);
    Room getRoom(String username, String roomID);

    Cabinet getCabinet(String username, String locationName,
                       String roomName, String cabinetName);

    Cabinet getCabinet(String username, String cabinetID);

    Chemical getChemical(String name);

    List<Location> getLocations(String username);

    List<Room> getRooms(String username, String locationName);

    List<Cabinet> getCabinets(String username, String locationName, String roomName);

    List<Chemical> getChemicals(String username, String locationName,
                                String roomName, String cabinetName);

    List<Cabinet> getCabinetsByIds(String username, List<String> roomIDs);

    List<Chemical> getChemicalsByName(List<String> chemicalNames);

    List<Room> getRoomsByIds(String username, List<String> locationIDs);

    List<Location> getAllLocationsForUser(String username);
    List<Room> getAllRoomsForUser(String username);

    List<Chemical> searchPartialChemicalName(String name);
    List<Chemical> searchPartialChemicalName(List<String> names);

    void addLocation(Location location);
    void addRoom(Room room, String parentID);
    void addCabinet(Cabinet cabinet, String parentID);

    void removeLocation(Location location);
    void removeRoom(Room room, String parentID);
    void removeCabinet(Cabinet cabinet, String parentID);

    void updateLocation(Location location);
    void updateRoom(Room room);
    void updateCabinet(Cabinet cabinet);

    void forkCabinet(Cabinet cabinet, String version);

    Comparison compareCabinetVersions(Cabinet cabinet, String oldVersion, String newVersion);
}

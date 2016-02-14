package com.chemicaltracker.service;


import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;

import java.util.List;

public interface InventoryService {

    Location getLocation(String username, String locationName);

    Room getRoom(String username, String locationName, String roomName);

    Cabinet getCabinet(String username, String locationName,
                       String roomName, String cabinetName);

    Chemical getChemical(String name);

    List<Location> getLocations(String username);

    List<Room> getRooms(String username, String locationName);

    List<Cabinet> getCabinets(String username, String locationName, String roomName);

    List<Chemical> getChemicals(String username, String locationName,
                                String roomName, String cabinetName);

    List<Cabinet> getCabinetsByIds(String username, List<String> roomIDs);

    List<Chemical> getChemicalsByName(List<String> chemicalNames);

    List<Room> getRoomsByIds(String username, List<String> locationIDs);

}

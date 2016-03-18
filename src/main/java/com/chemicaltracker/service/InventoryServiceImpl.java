package com.chemicaltracker.service;

import com.chemicaltracker.model.Comparison;
import com.chemicaltracker.persistence.dao.CabinetDao;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.util.CabinetEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service Layer Pattern
 * Used to separate the logic between the backed (Data Access Objects)
 * and the controllers.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private static final LocationDao locationsDB    = LocationDao.getInstance();
    private static final CabinetDao cabinetsDB      = CabinetDao.getInstance();
    private static final ChemicalDao chemicalsDB    = ChemicalDao.getInstance();
    private static final RoomDao roomsDB            = RoomDao.getInstance();

    private static final CabinetEvaluator evaluator = new CabinetEvaluator();

    @Override
    public List<Chemical> getChemicals(final String username, final String locationName,
                                              final String roomName, final String cabinetName) {

        final String roomID = getLocation(username, locationName).getStoredItemID(roomName);
        final String cabinetID = roomsDB.find(username, roomID).getStoredItemID(cabinetName);
        final Cabinet cabinet = cabinetsDB.find(username, cabinetID);
        final List<String> chemicalNames = cabinet.getStoredItemNames();

        final List<Chemical> chemicals = chemicalsDB.findByNames(chemicalNames);
        chemicals.forEach(c -> c.setImageURL(cabinet.getStoredItemID(c.getName())));

        return chemicals;
    }

    @Override
    public List<Cabinet> getCabinetsByIds(final String username, final List<String> roomIDs) {
        return cabinetsDB.findAllByIds(username, roomIDs);
    }

    @Override
    public List<Chemical> getChemicalsByName(final List<String> chemicalNames) {
        return chemicalsDB.findByNames(chemicalNames);
    }

    @Override
    public List<Room> getRoomsByIds(final String username, final List<String> locationIDs) {
        return roomsDB.findAllByIds(username, locationIDs);
    }

    @Override
    public List<Location> getAllLocationsForUser(String username) {
        return locationsDB.findAll(username);
    }

    @Override
    public List<Room> getAllRoomsForUser(String username) {
        return roomsDB.findAll(username);
    }

    @Override
    public List<Chemical> searchPartialChemicalName(String name) {
        return chemicalsDB.searchPartialChemicalName(name);
    }

    @Override
    public List<Chemical> searchPartialChemicalName(List<String> names) {
        return chemicalsDB.searchPartialChemicalName(names);
    }

    @Override
    public void addLocation(final Location location) {
        locationsDB.create(location);
    }

    @Override
    public void addRoom(final Room room, final String parentID) {
        final Location location = locationsDB.find(room.getUsername(), parentID);
        location.addStoredItem(room.getName(), room.getID());
        locationsDB.update(location);
        roomsDB.create(room);
    }

    @Override
    public void addCabinet(final Cabinet cabinet, final String parentID) {
        final Room room = roomsDB.find(cabinet.getUsername(), parentID);
        room.addStoredItem(cabinet.getName(), cabinet.getID());
        roomsDB.update(room);
        cabinetsDB.create(cabinet);
    }

    @Override
    public void removeLocation(final Location location) {
        locationsDB.delete(location);
    }

    @Override
    public void removeRoom(final Room room, final String parentID) {
        final Location location = locationsDB.find(room.getUsername(), parentID);
        location.removeStoredItem(room.getName());
        locationsDB.update(location);
        roomsDB.delete(room);
    }

    @Override
    public void removeCabinet(final Cabinet cabinet, final String parentID) {
        final Room room = roomsDB.find(cabinet.getUsername(), parentID);
        LOGGER.info("CABINET ID: " + cabinet.getID());
        LOGGER.info("Removing cabinet name: " + cabinet.getName() + " from room: " + room.getName());
        room.removeStoredItem(cabinet.getName());
        roomsDB.update(room);
        cabinetsDB.delete(cabinet);
    }

    @Override
    public void updateLocation(final Location location) {
        locationsDB.update(location);
    }

    @Override
    public void updateRoom(final Room room) {
        roomsDB.update(room);
    }

    @Override
    public void updateCabinet(final Cabinet cabinet) {
        cabinetsDB.update(evaluator.evaluate(cabinet));
    }

    @Override
    public void forkCabinet(final Cabinet cabinet, final String version, final boolean withChemicals) {
        cabinet.forkVersion(version, withChemicals);
        cabinetsDB.update(cabinet);
    }

    @Override
    public Comparison compareCabinetVersions(final Cabinet cabinet, final String oldVersion, final String newVersion) {

        if (cabinet.getAuditVersion(oldVersion.toLowerCase()) == null ||
                cabinet.getAuditVersion(newVersion.toLowerCase()) == null) {
            return new Comparison(); // in the future, might want to throw an error if versions are invalid
        }

        final Set<String> oldSet = new HashSet<>(cabinet.getAuditVersion(oldVersion).getChemicalNames());
        final Set<String> newSet = new HashSet<>(cabinet.getAuditVersion(newVersion).getChemicalNames());

        Set<String> inBoth = new HashSet<>(oldSet);
        inBoth.retainAll(newSet);

        Set<String> removed = new HashSet<>(oldSet);
        removed.removeAll(newSet);

        Set<String> added = new HashSet<>(newSet);
        added.removeAll(oldSet);

        return new Comparison(added, removed, inBoth);
    }

    @Override
    public Location getLocation(final String username, final String locationName) {
        return locationsDB.find(username, locationName);
    }

    @Override
    public Room getRoom(final String username, final String locationName, final String roomName) {
        final String roomID = getLocation(username, locationName).getStoredItemID(roomName);
        if (roomID == null || roomID.isEmpty()) {
            return null;
        }
        return roomsDB.find(username, roomID);
    }

    @Override
    public Room getRoom(String username, String roomID) {
        return roomsDB.find(username, roomID);
    }

    @Override
    public Cabinet getCabinet(final String username, final String locationName, final String roomName, final String cabinetName) {
        final String roomID = locationsDB.find(username, locationName).getStoredItemID(roomName);
        if (roomID == null || roomID.isEmpty()) return null;
        final String cabinetID = roomsDB.find(username, roomID).getStoredItemID(cabinetName);
        if (cabinetID == null || cabinetID.isEmpty()) return null;
        return cabinetsDB.find(username, cabinetID);
    }

    @Override
    public Cabinet getCabinet(String username, String cabinetID) {
        return cabinetsDB.find(username, cabinetID);
    }

    @Override
    public Chemical getChemical(final String name) {
        return chemicalsDB.find(name);
    }

    @Override
    public List<Location> getLocations(final String username) {
        return locationsDB.findAll(username);
    }

    @Override
    public List<Room> getRooms(final String username, final String locationName) {
        final List<String> roomIDs = getLocation(username, locationName).getStoredItemIDs();
        return roomsDB.findAllByIds(username, roomIDs);
    }

    @Override
    public List<Cabinet> getCabinets(final String username, final String locationName, final String roomName) {
        final Room room = getRoom(username, locationName, roomName);
        if (room == null) return new ArrayList<>();
        final List<String> cabinetIDs = room.getStoredItemIDs();
        return cabinetsDB.findAllByIds(username, cabinetIDs);
    }
}

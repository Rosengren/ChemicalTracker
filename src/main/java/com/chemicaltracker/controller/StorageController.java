package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.model.Storage;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class StorageController {

    private StorageDataAccessObject locationDB = StorageFactory.getStorage("LOCATIONS");
    private StorageDataAccessObject roomDB = StorageFactory.getStorage("ROOMS");
    private StorageDataAccessObject cabinetDB = StorageFactory.getStorage("CABINETS");

    @RequestMapping(value={"/locations"})
    public String home(Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Locations",
                "List of all your locations", "/rooms", "Add new location", "/api/add/location");

        // Get Locations
        model.addAttribute("storages", locationDB.getAllStoragesForUser(username));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"locations"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storageView";
    }

    @RequestMapping("/locations/{locationName}/rooms")
    public String rooms(@PathVariable("locationName") String locationName, Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Rooms",
                "List of all the rooms in " + locationName, "/cabinets",
                "Add new room", "/api/add/room/to/location/" + locationName);

        Storage location = locationDB.getStorage(username, locationName);
        List<String> roomIDs = new ArrayList<String>();

        for (Map.Entry<String, String> entry : location.getStoredItemIDs().entrySet()) {
            roomIDs.add(entry.getValue());
        }

        model.addAttribute("storages", roomDB.batchGetStorages(username, roomIDs));

        // TODO; make more efficient
        final List<String> breadcrumbs = Arrays.asList(new String[] {"locations", locationName, "rooms"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storageView";
    }

    @RequestMapping("/locations/{locationName}/rooms/{roomName}/cabinets")
    public String cabinets(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.getStorage(username, locationName).getStoredItemIDs().get(roomName);

        model = addStorageDetailsToModel(model, username, "Cabinets",
                "List of all the cabinets in " + roomName, "/chemicals",
                "Add new cabinet", "/api/add/cabinet/to/room/" + roomID);

        Storage room = roomDB.getStorage(username, roomID);

        List<String> cabinetIDs = new ArrayList<String>();

        for (Map.Entry<String, String> entry : room.getStoredItemIDs().entrySet()) {
            cabinetIDs.add(entry.getValue());
        }

        model.addAttribute("storages", cabinetDB.batchGetStorages(username, cabinetIDs));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"locations" ,
            locationName , "rooms" , roomName , "cabinets"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storageView";
    }

    @RequestMapping("/locations/{locationName}/rooms/{roomName}/cabinets/{cabinetName}/chemicals")
    public String chemicals(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, @PathVariable("cabinetName") String cabinetName,
            Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.getStorage(username, locationName).getStoredItemIDs().get(roomName);
        String cabinetID = roomDB.getStorage(username, roomID).getStoredItemIDs().get(cabinetName);

        model = addStorageDetailsToModel(model, username, "Chemicals",
                "List of all the chemicals in " + cabinetName, "/chemical",
                "Add chemical", "/api/add/chemical/to/cabinet/" + cabinetID);

        Storage cabinet = cabinetDB.getStorage(username, cabinetID);

        List<String> chemicalNames = new ArrayList<String>(cabinet.getStoredItemIDs().keySet());

        final List<String> breadcrumbs = Arrays.asList(new String[] {"locations" ,
            locationName , "rooms" , roomName , "cabinets" , cabinetName , "chemicals"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storageView";
    }

    private Model addStorageDetailsToModel(final Model model, final String username, final String title,
            final String subtitle, final String subStorageLink, final String addTooltip, final String addURL) {
        model.addAttribute("title", title);
        model.addAttribute("subtitle", subtitle);
        model.addAttribute("subStorageLink", subStorageLink);
        model.addAttribute("addTooltip", addTooltip);
        model.addAttribute("username", username);
        model.addAttribute("addURL", addURL);
        return model;
    }
}

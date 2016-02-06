package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import java.util.*;
import com.chemicaltracker.model.*;
import com.chemicaltracker.model.storage.*;
import com.chemicaltracker.persistence.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = {"/home", "/Home"})
public class StorageController {

    // private static final StorageDAO locationDB = StorageFactory.getStorage("LOCATIONS");
    private static final LocationDAO locationDB = LocationDAO.getInstance();
    private static final CabinetDAO cabinetDB = CabinetDAO.getInstance();
    private static final RoomDAO roomDB = RoomDAO.getInstance();

    private ChemicalDAO chemicalDB = ChemicalDAO.getInstance();

    @RequestMapping("")
    public String home(Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Locations",
                "List of all your locations", "Add new location", "/add/location", "");

        // Get Locations
        model.addAttribute("storages", locationDB.findAll(username));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "locationsView";
    }

    @RequestMapping("/{locationName}")
    public String rooms(@PathVariable("locationName") String locationName, Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Rooms",
                "List of all the rooms in " + locationName,
                "Add new room", "/add/room/", "");

        final Location location = locationDB.find(username, locationName);
        final List<String> roomIDs = location.getStoredItemIDs();

        model.addAttribute("storages", roomDB.findAllRooms(username, roomIDs));

        model.addAttribute("breadcrumbs", Arrays.asList(new String[] {"Home", locationName}));

        model.addAttribute("parentID", location.getID());

        return "roomsView";
    }

    @RequestMapping("/{locationName}/{roomName}")
    public String cabinets(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.find(username, locationName).getStoredItemID(roomName);

        model = addStorageDetailsToModel(model, username, "Cabinets",
                "List of all the cabinets in " + roomName, "Add new cabinet",
                "/add/cabinet/", "");

        final Room room = roomDB.find(username, roomID);
        final List<String> cabinetIDs = room.getStoredItemIDs();

        model.addAttribute("storages", cabinetDB.findAllCabinets(username, cabinetIDs));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home" ,
            locationName , roomName});
        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("parentID", room.getID());

        return "cabinetsView";
    }

    @RequestMapping("/{locationName}/{roomName}/{cabinetName}")
    public String chemicals(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, @PathVariable("cabinetName") String cabinetName,
            Model model, Principal principal) {

        final String username = principal.getName();
        final String roomID = locationDB.find(username, locationName).getStoredItemID(roomName);
        final String cabinetID = roomDB.find(username, roomID).getStoredItemID(cabinetName);

        model = addStorageDetailsToModel(model, username, "Chemicals",
                "List of all the chemicals in " + cabinetName,
                "Add chemical", "/api/add/chemicals/to/cabinet/" + cabinetID,
                "/api/remove/chemical/from/cabinet/" + cabinetID);

        final Cabinet cabinet = cabinetDB.find(username, cabinetID);
        final List<String> chemicalNames = cabinet.getStoredItemNames();
        final List<Chemical> chemicals = chemicalDB.batchGetChemicals(chemicalNames);

        final Set<String> checklist = new HashSet<String>();
        for (Chemical chemical : chemicals) {
            chemical.setImageURL(cabinet.getStoredItemID(chemical.getName()));
            checklist.add(chemical.getHandlingAndStorage().get("Storage"));
        }

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home" ,
            locationName , roomName , cabinetName});
        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("tags", cabinet.getTags());
        model.addAttribute("checklist", checklist);


        model.addAttribute("searchChemicalURL", "/api/test/partialQuery/");
        model.addAttribute("chemicals", chemicals);

        return "chemicalsView";
    }

    @RequestMapping(value = "/{locationName}/{roomName}/{cabinetName}/{chemicalName}")
    public String chemical(@PathVariable("locationName") String locationName, @PathVariable("roomName") String roomName,
            @PathVariable("cabinetName") String cabinetName, @PathVariable("chemicalName") String chemicalName,
            Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.find(username, locationName).getStoredItemID(roomName);
        String cabinetID = roomDB.find(username, roomID).getStoredItemID(cabinetName);

        model = addStorageDetailsToModel(model, username, chemicalName, "Material Safety Data Sheet",
                "Edit Chemical", "/api/update/chemical/" + chemicalName, "");

        final Chemical chemical = chemicalDB.find(chemicalName);
        model.addAttribute("chemical", chemical);

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home",
            locationName, roomName, cabinetName, chemicalName});

        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("fireDiamond", chemical.getFireDiamond());

        return "chemicalView";
    }

    private Model addStorageDetailsToModel(final Model model, final String username, final String title,
            final String subtitle, final String addTooltip, final String addURL,
            final String removeURL) {
        model.addAttribute("title", title);
        model.addAttribute("subtitle", subtitle);
        model.addAttribute("addTooltip", addTooltip);
        model.addAttribute("username", username);
        model.addAttribute("addURL", addURL);
        model.addAttribute("removeURL", removeURL);
        return model;
    }
}

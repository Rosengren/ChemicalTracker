package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.persistence.StorageDataAccessDynamoDB;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

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
@RequestMapping(value = {"/home", "/Home"})
public class StorageController {

    private StorageDataAccessObject locationDB = StorageFactory.getStorage("LOCATIONS");
    private StorageDataAccessObject roomDB = StorageFactory.getStorage("ROOMS");
    private StorageDataAccessObject cabinetDB = StorageFactory.getStorage("CABINETS");

    private ChemicalDataAccessObject chemicalDB = new ChemicalDataAccessDynamoDB();

    @RequestMapping("")
    public String home(Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Locations",
                "List of all your locations", "Add new location", "/add/location");

        // Get Locations
        model.addAttribute("storages", locationDB.getAllStoragesForUser(username));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home"});
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storageView";
    }

    @RequestMapping("/{locationName}")
    public String rooms(@PathVariable("locationName") String locationName, Model model, Principal principal) {

        String username = principal.getName();
        model = addStorageDetailsToModel(model, username, "Rooms",
                "List of all the rooms in " + locationName,
                "Add new room", "/add/room/");

        final Storage location = locationDB.getStorage(username, locationName);
        final List<String> roomIDs = location.getStoredItemIDs();

        model.addAttribute("storages", roomDB.batchGetStorages(username, roomIDs));

        // TODO; make more efficient
        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home", locationName});
        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("parentID", location.getID());

        return "storageView";
    }

    @RequestMapping("/{locationName}/{roomName}")
    public String cabinets(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.getStorage(username, locationName).getStoredItemID(roomName);

        model = addStorageDetailsToModel(model, username, "Cabinets",
                "List of all the cabinets in " + roomName, "/add/room",
                "Add new cabinet");

        final Storage room = roomDB.getStorage(username, roomID);
        final List<String> cabinetIDs = room.getStoredItemIDs();

        model.addAttribute("storages", cabinetDB.batchGetStorages(username, cabinetIDs));

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home" ,
            locationName , roomName});
        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("parentID", room.getID());

        return "storageView";
    }

    @RequestMapping("/{locationName}/{roomName}/{cabinetName}")
    public String chemicals(@PathVariable("locationName") String locationName,
            @PathVariable("roomName") String roomName, @PathVariable("cabinetName") String cabinetName,
            Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.getStorage(username, locationName).getStoredItemID(roomName);
        String cabinetID = roomDB.getStorage(username, roomID).getStoredItemID(cabinetName);

        model = addStorageDetailsToModel(model, username, "Chemicals",
                "List of all the chemicals in " + cabinetName,
                "Add chemical", "/api/add/chemicals/to/cabinet/" + cabinetID);

        final Storage cabinet = cabinetDB.getStorage(username, cabinetID);
        final List<String> chemicalNames = cabinet.getStoredItemNames();

        final List<Chemical> chemicals = chemicalDB.batchGetChemicals(chemicalNames);

        for (Chemical chemical : chemicals) {
            chemical.setImageURL(cabinet.getStoredItemID(chemical.getName()));
        }

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home" ,
            locationName , roomName , cabinetName});
        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("chemicalNames", chemicalDB.getAllChemicalNames());
        model.addAttribute("chemicals", chemicals);

        return "chemicalsView";
    }

    @RequestMapping(value = "/{locationName}/{roomName}/{cabinetName}/{chemicalName}")
    public String chemical(@PathVariable("locationName") String locationName, @PathVariable("roomName") String roomName,
            @PathVariable("cabinetName") String cabinetName, @PathVariable("chemicalName") String chemicalName,
            Model model, Principal principal) {

        String username = principal.getName();
        String roomID = locationDB.getStorage(username, locationName).getStoredItemID(roomName);
        String cabinetID = roomDB.getStorage(username, roomID).getStoredItemID(cabinetName);

        model = addStorageDetailsToModel(model, username, chemicalName, "Material Safety Data Sheet",
                "Edit Chemical", "/api/update/chemical/" + chemicalName);

        final Chemical chemical = chemicalDB.getChemical(chemicalName);
        model.addAttribute("chemical", chemical);

        final List<String> breadcrumbs = Arrays.asList(new String[] {"Home",
            locationName, roomName, cabinetName, chemicalName});

        model.addAttribute("breadcrumbs", breadcrumbs);

        model.addAttribute("fireDiamond", chemical.getFireDiamond());

        return "chemicalView";
    }

    private Model addStorageDetailsToModel(final Model model, final String username, final String title,
            final String subtitle, final String addTooltip, final String addURL) {
        model.addAttribute("title", title);
        model.addAttribute("subtitle", subtitle);
        model.addAttribute("addTooltip", addTooltip);
        model.addAttribute("username", username);
        model.addAttribute("addURL", addURL);
        return model;
    }
}

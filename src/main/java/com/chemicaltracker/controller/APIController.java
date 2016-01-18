package com.chemicaltracker.controller;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

import com.chemicaltracker.model.Storage;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;

import org.springframework.ui.Model;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api")
public class APIController {

    private static final StorageDataAccessObject locationDB =
        StorageFactory.getStorage("LOCATIONS");
    private static final StorageDataAccessObject roomDB =
        StorageFactory.getStorage("ROOMS");
    private static final StorageDataAccessObject cabinetDB =
        StorageFactory.getStorage("CABINETS");

    private ChemicalDataAccessObject chemicalDB = ChemicalDataAccessDynamoDB.getInstance();

    // TODO: Below may need to be deprecated
    @RequestMapping(value="/add/location", method=POST)
    public @ResponseBody String addLocation(@RequestBody final Storage location, BindingResult result,
            Model model, Principal principal) {
        location.setID(location.getName());
        locationDB.addStorage(location);
        return "success";
    }

    @RequestMapping(value="/add/room/to/location/{locationName}", method=POST)
    public @ResponseBody String addRoom(@PathVariable("locationName") final String locationName,
            @RequestBody final Storage room, BindingResult result, Model model, Principal principal) {

        Storage location = locationDB.getStorage(principal.getName(), locationName);

        String uuid = UUID.randomUUID().toString();
        room.setID(uuid);

        location.addStoredItem(room.getName(), uuid);
        locationDB.addStorage(location);

        roomDB.addStorage(room);

        return "success";
    }

    @RequestMapping(value="/add/cabinet/to/room/{roomID}")
    public @ResponseBody String addCabinet(@PathVariable("roomID") final String roomID,
            @RequestBody final Storage cabinet, BindingResult result, Model model, Principal principal) {

        Storage room = roomDB.getStorage(principal.getName(), roomID);

        String uuid = UUID.randomUUID().toString();
        cabinet.setID(uuid);

        room.addStoredItem(cabinet.getName(), uuid);
        roomDB.addStorage(room);

        cabinetDB.addStorage(cabinet);

        return "success";
    }

    @RequestMapping(value="/add/chemicals/to/cabinet/{cabinetID}")
    public @ResponseBody String addChemical(@PathVariable("cabinetID") final String cabinetID,
            @RequestBody final List<String> chemicalNames, BindingResult result, Model model, Principal principal) {

        Storage cabinet = cabinetDB.getStorage(principal.getName(), cabinetID);

        for (String chemicalName : chemicalNames) {
            cabinet.addStoredItem(chemicalName, "0");
        }

        cabinetDB.addStorage(cabinet);

        return "success";
    }
}

package com.chemicaltracker.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;

import com.chemicaltracker.util.FireDiamondEvaluator;
import com.chemicaltracker.util.StorageEvaluator;

import com.chemicaltracker.model.*;
import com.chemicaltracker.model.storage.*;
import com.chemicaltracker.model.request.*;
import com.chemicaltracker.model.response.*;
import com.chemicaltracker.persistence.*;

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

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api")
public class APIController {

    private static final StorageEvaluator storageEvaluator =
        new StorageEvaluator();

    private static final FireDiamondEvaluator fireDiamondEvaluator =
        new FireDiamondEvaluator();

    private static final LocationDAO locationDB = LocationDAO.getInstance();
    private static final RoomDAO roomDB = RoomDAO.getInstance();
    private static final CabinetDAO cabinetDB = CabinetDAO.getInstance();

    private ChemicalDAO chemicalDB = ChemicalDAO.getInstance();

    private UserDAO userDB = UserDAO.getInstance();

    // @RequestMapping(value="/add/user", method=POST)
    // public @ResponseBody String addUser(@RequestBody final User user) {
    //     // userDB.createUser(user.getUsername(), user.getPassword(), user.getRole());
    //     userDB.create(user);
    //     return "success";
    // }

    @RequestMapping(value="/login", method=POST)
    public @ResponseBody ResponseEntity login() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/update", method=POST)
    public @ResponseBody UpdateResponse update(@RequestBody final UpdateRequest request, BindingResult result,
            Model model, Principal principal) {

        final String requestType = request.getRequestType();

        if (!requestType.equals("ADD") && !requestType.equals("REMOVE")) {
            return new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        } else if (request.getLocation().equals("invalid") || request.getLocation().equals("") ||
                    request.getRoom().equals("invalid") || request.getRoom().equals("") ||
                    request.getCabinet().equals("invalid") || request.getCabinet().equals("")) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        } else {
            if (request.getChemical().equals("valid")) {
                if (requestType.equals("ADD")) {
                    return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
                } else {
                    return new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
                }
            } else {
                final Chemical chemical = chemicalDB.find(request.getChemical());
                if (chemical.getName().equals("")) {
                    return new UpdateResponse(UpdateStatus.INVALID_CHEMICAL);
                } else {
                    if (requestType.equals("ADD")) {
                        return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
                    } else {
                        return new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
                    }
                }
            }
        }
    }

    @RequestMapping(value="/query", method=POST)
    public @ResponseBody ChemicalResponse queryRequest(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final Chemical chemical = chemicalDB.find(request.getChemical());

        final ChemicalResponse.Properties properties =
            new ChemicalResponse.Properties()
                .withFireDiamond(chemical.getFireDiamond());

        return new ChemicalResponse()
                    .withMatch(chemical.getMatch())
                    .withChemical(chemical.getName())
                    .withProperties(properties);
    }

    // TODO replace responseBody with response object
    @RequestMapping(value="/partialQuery", method=POST)
    public @ResponseBody String partialQueryRequest(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final List<Chemical> chemicals = chemicalDB.searchPartialChemicalName(request.getChemical());

        List<String> chemicalNames = new ArrayList<String>();
        for (Chemical chemical : chemicals) {
            chemicalNames.add(chemical.getName());
        }

        return chemicalNames.toString();
    }

    // TODO: Below may need to be deprecated
    @RequestMapping(value="/add/location", method=POST)
    public @ResponseBody String addLocation(@RequestBody final Location location, BindingResult result,
            Model model, Principal principal) {
        location.setID(location.getName());
        locationDB.create(location);

        return "success";
    }

    @RequestMapping(value="/add/room/to/location/{locationName}", method=POST)
    public @ResponseBody String addRoom(@PathVariable("locationName") final String locationName,
            @RequestBody final Room room, BindingResult result, Model model, Principal principal) {

        final Location location = locationDB.find(principal.getName(), locationName);

        final String uuid = UUID.randomUUID().toString();
        room.setID(uuid);

        location.addStoredItem(room.getName(), uuid);
        locationDB.create(location);

        roomDB.create(room);

        return "success";
    }

    @RequestMapping(value="/add/cabinet/to/room/{roomID}")
    public @ResponseBody String addCabinet(@PathVariable("roomID") final String roomID,
            @RequestBody final Cabinet cabinet, BindingResult result, Model model, Principal principal) {

        final Room room = roomDB.find(principal.getName(), roomID);

        final String uuid = UUID.randomUUID().toString();
        cabinet.setID(uuid);

        room.addStoredItem(cabinet.getName(), uuid);
        roomDB.update(room);

        cabinetDB.create(cabinet);

        return "success";
    }

    @RequestMapping(value="/add/chemicals/to/cabinet/{cabinetID}")
    public @ResponseBody UpdateResponse addChemicalToCabinet(@PathVariable("cabinetID") final String cabinetID,
            @RequestBody final List<String> chemicalNames, BindingResult result, Model model, Principal principal) {

        Cabinet cabinet = cabinetDB.find(principal.getName(), cabinetID);

        for (String chemicalName : chemicalNames) {
            cabinet.addStoredItem(chemicalName, "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png");
        }

        cabinet = evaluateCabinet(cabinet);
        cabinetDB.update(cabinet);
        return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
    }

    @RequestMapping(value="/remove/chemical/from/cabinet/{cabinetID}")
    public @ResponseBody String removeChemicalFromCabinet(@PathVariable("cabinetID") final String cabinetID,
        @RequestBody final RemoveChemicalRequest request, BindingResult result, Model model, Principal principal) {

        Cabinet cabinet = cabinetDB.find(principal.getName(), cabinetID);
        cabinet.removeStoredItem(request.getChemicalName());

        cabinet = evaluateCabinet(cabinet);

        cabinetDB.update(cabinet);

        return "success";
    }

    @RequestMapping(value="/success")
    public @ResponseBody String success() {
        return "success";
    }

    // TODO: move this to a new class
    private Cabinet evaluateCabinet(final Cabinet cabinet) {

        final List<Chemical> chemicals = 
            chemicalDB.batchGetChemicals(cabinet.getStoredItemNames());

        if (fireDiamondEvaluator.checkFlammability(chemicals)) {
            cabinet.addTag(StorageTag.FLAMMABLE);
        }

        if (fireDiamondEvaluator.checkInstability(chemicals)) {
            cabinet.addTag(StorageTag.UNSTABLE);
        }

        if (fireDiamondEvaluator.checkHealth(chemicals)) {
            cabinet.addTag(StorageTag.HEALTH);
        }

        boolean hasOxidizingAgents = false;
        if (storageEvaluator.containsOxidizingAgent(cabinet.getStoredItemNames())) {
            cabinet.addTag(StorageTag.OXIDIZING_AGENTS);
            hasOxidizingAgents = true;
        }

        boolean hasReductionAgents = false;
        if (storageEvaluator.containsReductionAgent(cabinet.getStoredItemNames())) {
            cabinet.addTag(StorageTag.REDUCTION_AGENTS);
            hasReductionAgents = true;
        }

        if (hasReductionAgents && hasOxidizingAgents) {
            cabinet.addTag(StorageTag.INCOMPATIBLE);
        }

        boolean hasBases = false;
        if (storageEvaluator.containsBasics(chemicals)) {
            cabinet.addTag(StorageTag.BASIC);
            hasBases = true;
        }

        boolean hasAcids = false;
        if (storageEvaluator.containsAcids(chemicals)) {
            cabinet.addTag(StorageTag.ACIDIC);
            hasAcids = true;
        }

        if (hasAcids && hasBases) {
            cabinet.addTag(StorageTag.ACIDS_AND_BASES);
        }

        return cabinet;
    }
}

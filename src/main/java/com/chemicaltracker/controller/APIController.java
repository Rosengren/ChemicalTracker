package com.chemicaltracker.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;

import com.chemicaltracker.util.FireDiamondEvaluator;
import com.chemicaltracker.util.StorageEvaluator;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.UpdateStatus;
import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.request.*;
import com.chemicaltracker.model.response.*;
import com.chemicaltracker.model.User;
import com.chemicaltracker.model.StorageTag;

import com.chemicaltracker.persistence.ChemicalDAO;
import com.chemicaltracker.persistence.UserDAO;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDAO;

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

    private static final StorageEvaluator storageEvaluator =
        new StorageEvaluator();

    private static final FireDiamondEvaluator fireDiamondEvaluator =
        new FireDiamondEvaluator();

    private static final StorageDAO locationDB = StorageFactory.getStorage("LOCATIONS");
    private static final StorageDAO roomDB = StorageFactory.getStorage("ROOMS");
    private static final StorageDAO cabinetDB = StorageFactory.getStorage("CABINETS");

    private ChemicalDAO chemicalDB = ChemicalDAO.getInstance();

    private UserDAO userDB = UserDAO.getInstance();

    @RequestMapping(value="/add/user", method=POST)
    public @ResponseBody String addUser(@RequestBody final User user) {
        // userDB.createUser(user.getUsername(), user.getPassword(), user.getRole());
        userDB.create(user);
        return "success";
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
    public @ResponseBody String addLocation(@RequestBody final Storage location, BindingResult result,
            Model model, Principal principal) {
        location.setID(location.getName());
        locationDB.addStorage(location);
        return "success";
    }

    @RequestMapping(value="/add/room/to/location/{locationName}", method=POST)
    public @ResponseBody String addRoom(@PathVariable("locationName") final String locationName,
            @RequestBody final Storage room, BindingResult result, Model model, Principal principal) {

        final Storage location = locationDB.getStorage(principal.getName(), locationName);

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
    public @ResponseBody UpdateResponse addChemicalToCabinet(@PathVariable("cabinetID") final String cabinetID,
            @RequestBody final List<String> chemicalNames, BindingResult result, Model model, Principal principal) {

        Storage cabinet = cabinetDB.getStorage(principal.getName(), cabinetID);

        for (String chemicalName : chemicalNames) {
            cabinet.addStoredItem(chemicalName, "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png");
        }

        cabinet = evaluateCabinet(cabinet);
        cabinetDB.addStorage(cabinet);
        return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
    }

    @RequestMapping(value="/remove/chemical/from/cabinet/{cabinetID}")
    public @ResponseBody String removeChemicalFromCabinet(@PathVariable("cabinetID") final String cabinetID,
        @RequestBody final RemoveChemicalRequest request, BindingResult result, Model model, Principal principal) {

        Storage cabinet = cabinetDB.getStorage(principal.getName(), cabinetID);
        cabinet.removeStoredItem(request.getChemicalName());

        cabinet = evaluateCabinet(cabinet);

        cabinetDB.addStorage(cabinet);

        return "success";
    }

    @RequestMapping(value="/success")
    public @ResponseBody String success() {
        return "success";
    }

    // TODO: move this to a new class
    private Storage evaluateCabinet(final Storage cabinet) {

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

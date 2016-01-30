package com.chemicaltracker.controller;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

import com.chemicaltracker.util.Evaluator;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.ChemicalQueryRequest;
import com.chemicaltracker.model.User;

import com.chemicaltracker.model.RemoveChemicalRequest;

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

    private static final StorageDAO locationDB =
        StorageFactory.getStorage("LOCATIONS");
    private static final StorageDAO roomDB =
        StorageFactory.getStorage("ROOMS");
    private static final StorageDAO cabinetDB =
        StorageFactory.getStorage("CABINETS");

    private ChemicalDAO chemicalDB = ChemicalDAO.getInstance();

    private UserDAO userDB = UserDAO.getInstance();

    @RequestMapping(value="/add/user", method=POST)
    public @ResponseBody String addUser(@RequestBody final User user) {
        userDB.createUser(user.getUsername(), user.getPassword(), user.getRole());
        return "success";
    }

    @RequestMapping(value="/query", method=POST)
    public @ResponseBody String testQuery(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final Chemical chemical = chemicalDB.getChemical(request.getChemical());
        return chemical.toJSONString();
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
    public @ResponseBody String addChemicalToCabinet(@PathVariable("cabinetID") final String cabinetID,
            @RequestBody final List<String> chemicalNames, BindingResult result, Model model, Principal principal) {

        Storage cabinet = cabinetDB.getStorage(principal.getName(), cabinetID);

        for (String chemicalName : chemicalNames) {
            cabinet.addStoredItem(chemicalName, "https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png");
        }

        cabinet = evaluateCabinet(cabinet);
        cabinetDB.addStorage(cabinet);
        return "success";
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

    private Storage evaluateCabinet(Storage cabinet) {
        // TODO: evaluate for flammability and stuff
        // then add tags

        List<Chemical> chemicals = 
            chemicalDB.batchGetChemicals(cabinet.getStoredItemNames());

        Evaluator evaluator = new Evaluator();

        if (evaluator.checkFlammability(chemicals)) {
            cabinet.addTag("FLAMMABLE");
        }

        return cabinet;
    }
}

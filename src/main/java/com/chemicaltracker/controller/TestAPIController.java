package com.chemicaltracker.controller;

import java.util.*;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.UpdateStatus;
import com.chemicaltracker.model.request.*;
import com.chemicaltracker.model.response.*;

import com.chemicaltracker.persistence.ChemicalDAO;

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
@RequestMapping("/api/test")
public class TestAPIController {

    private static final ChemicalDAO chemicalDB = ChemicalDAO.getInstance();
    private static final StorageDAO locationDB = StorageFactory.getStorage("LOCATIONS");
    private static final StorageDAO roomDB = StorageFactory.getStorage("ROOMS");
    private static final StorageDAO cabinetDB = StorageFactory.getStorage("CABINETS");
    
    @RequestMapping(value="/success")
    public @ResponseBody String success() {
        return "success";
    }

    @RequestMapping(value="/update", method=POST)
    public @ResponseBody UpdateResponse testUpdate(@RequestBody final UpdateRequest request, BindingResult result,
            Model model, Principal principal) {

        final String requestType = request.getRequestType();

        if (!requestType.equals("ADD") && !requestType.equals("REMOVE")) {
            return new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        } else if (request.getUsername().equals("invalid") || request.getUsername().equals("")) {
            return new UpdateResponse(UpdateStatus.INVALID_USERNAME);
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
                final Chemical chemical = chemicalDB.getChemical(request.getChemical());
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
    public @ResponseBody String testQuery(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final Chemical chemical = chemicalDB.getChemical(request.getChemical());
        return chemical.toJSONString();
    }

    @RequestMapping(value="/partialQuery", method=POST)
    public @ResponseBody PartialChemicalQueryResponse partialQueryRequest(@RequestBody final PartialChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final List<Chemical> chemicals = chemicalDB.searchPartialChemicalName(request.getChemical());
        final PartialChemicalQueryResponse response = new PartialChemicalQueryResponse();
        for (Chemical chemical : chemicals) {
            response.addChemicalName(chemical.getName());
        }

        return response;
    }

    @RequestMapping(value="/userTree", method=POST)
    public @ResponseBody UserTreeResponse userTreeRequest(@RequestBody final UserTreeRequest request, BindingResult result,
        Model model, Principal principal) {

        // Instead of making multiple calls, get all of the 
        // rooms and map them to the appropriate locations
        final List<Storage> rooms = roomDB.getAllStoragesForUser(request.getUsername());
        final List<Storage> locations = locationDB.getAllStoragesForUser(request.getUsername());

        final Map<String, List<String>> roomMap = new HashMap<String, List<String>>();
        for (Storage room : rooms) {
            roomMap.put(room.getName(), room.getStoredItemNames());
        }

        final Map<String, Map<String, List<String>>> locationMap = new HashMap<String, Map<String, List<String>>>();
        for (Storage location : locations) {

            Map<String, List<String>> roomsInLocation = new HashMap<String, List<String>>();
            for (String roomName : location.getStoredItemNames()) {
                roomsInLocation.put(roomName, roomMap.get(roomName));
            }
            locationMap.put(location.getName(), roomsInLocation);
        }

        return new UserTreeResponse(locationMap);
    }
}

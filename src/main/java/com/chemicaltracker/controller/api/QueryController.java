package com.chemicaltracker.controller.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.chemicaltracker.controller.api.request.ChemicalQueryRequest;
import com.chemicaltracker.controller.api.response.ChemicalQueryResponse;
import com.chemicaltracker.controller.api.response.ChemicalResponse;
import com.chemicaltracker.controller.api.response.ChemicalSearchResponse;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api")
public class QueryController {

    private final InventoryService inventoryService;

    @Autowired
    public QueryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value="/query", method=POST)
    public @ResponseBody ChemicalResponse queryRequest(@RequestBody final ChemicalQueryRequest request) {

        final Chemical chemical = inventoryService.getChemical(request.getChemical());

        if (chemical == null) {
            return new ChemicalResponse().withMatch(false);
        }

        final ChemicalResponse.Properties properties =
                new ChemicalResponse.Properties()
                        .withFireDiamond(chemical.getFireDiamond());

        return new ChemicalResponse()
                .withMatch(chemical.getMatch())
                .withChemical(chemical.getName())
                .withProperties(properties);
    }

    @RequestMapping(value="/partialQuery", method=POST)
    public @ResponseBody ResponseEntity<List<String>> partialQueryRequest(
            @RequestBody final ChemicalQueryRequest request) {

        final List<Chemical> chemicals = inventoryService.searchPartialChemicalName(request.getChemical());

        final List<String> chemicalNames = chemicals.stream()
                .map(Chemical::getName)
                .collect(Collectors.toList());

        return new ResponseEntity<>(chemicalNames, HttpStatus.OK);
    }

    @RequestMapping(value="/partialQueries", method=POST)
    public @ResponseBody ChemicalQueryResponse partialQueriesRequest(@RequestBody final ChemicalQueryRequest request) {

        final List<Chemical> chemicals = inventoryService.searchPartialChemicalName(request.getChemicals());

        final List<String> chemicalNames = chemicals.stream()
                .map(Chemical::getName)
                .collect(Collectors.toList());

        return new ChemicalQueryResponse().withMatches(chemicalNames);
    }

    @RequestMapping(value="/userTree", method=POST)
    public @ResponseBody UserTreeResponse userTreeRequest(final Principal principal) {

        // Instead of making multiple calls, get all of the
        // rooms and map them to the appropriate locations
        final List<Room> rooms = inventoryService.getAllRoomsForUser(principal.getName());
        final List<Location> locations = inventoryService.getAllLocationsForUser(principal.getName());

        final Map<String, List<String>> roomMap = new HashMap<>();
        for (Room room : rooms) {
            roomMap.put(room.getName(), room.getStoredItemNames());
        }

        final Map<String, Map<String, List<String>>> locationMap = new HashMap<>();
        for (Location location : locations) {

            Map<String, List<String>> roomsInLocation = new HashMap<>();
            for (String roomName : location.getStoredItemNames()) {
                roomsInLocation.put(roomName, roomMap.get(roomName));
            }
            locationMap.put(location.getName(), roomsInLocation);
        }

        return new UserTreeResponse(locationMap);
    }

    @RequestMapping(value="/search/chemicals", method=GET)
    public ResponseEntity<ChemicalSearchResponse> searchForChemical(@RequestParam("q") final String query) {
        final List<Chemical> chemicals = inventoryService.searchPartialChemicalName(query);
        return new ResponseEntity<>(new ChemicalSearchResponse(chemicals), HttpStatus.OK);
    }


    private class UserTreeResponse {

        private Map<String, Map<String, List<String>>> locations;

        public UserTreeResponse(final Map<String, Map<String, List<String>>> locations) {
            this.locations = locations;
        }

        public Map<String, Map<String, List<String>>> getLocations() {
            return locations;
        }
    }
}

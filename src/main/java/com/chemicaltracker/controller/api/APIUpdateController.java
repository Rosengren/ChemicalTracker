package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.InventoryService;
import java.security.Principal;
import java.util.UUID;

// Annotations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api/update")
public class APIUpdateController {

    private final InventoryService inventoryService;

    @Autowired
    public APIUpdateController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value="/location", method=POST)
    public @ResponseBody UpdateResponse updateLocation(@RequestBody final UpdateRequest request, final Principal principal) {

        final String requestType = request.getRequest();

        if (invalidRequest(requestType)) {
            return new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        }

        if (missingLocation(request)) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        }

        if (requestType.toUpperCase().equals("ADD")) {

            // Check if location already exists
            Location l = inventoryService.getLocation(principal.getName(), request.getLocation());
            if (l != null) {
                return new UpdateResponse(UpdateStatus.STORAGE_ALREADY_EXISTS);
            }

            inventoryService.addLocation(new Location()
                .withUsername(principal.getName())
                .withName(request.getLocation())
                .withID(request.getLocation())
                .withDescription(" "));
            return new UpdateResponse(UpdateStatus.ADDED_LOCATION);
        } else if (requestType.toUpperCase().equals("REMOVE")) {

            final Location location = inventoryService.getLocation(principal.getName(), request.getLocation());
            if (location == null) {
                return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
            }
            inventoryService.removeLocation(location);
            return new UpdateResponse(UpdateStatus.REMOVED_LOCATION);
        }
        return new UpdateResponse(UpdateStatus.UNKNOWN_ERROR);
    }

    @RequestMapping(value="/room", method=POST)
    public @ResponseBody UpdateResponse updateRoom(@RequestBody final UpdateRequest request, final Principal principal) {

        final String requestType = request.getRequest();

        if (invalidRequest(requestType)) {
            return  new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        }

        if (missingLocationOrRoom(request)) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        }


        switch (requestType.toUpperCase()) {
            case "ADD": {
                final Location location = inventoryService.getLocation(principal.getName(), request.getLocation());
                if (location == null) {
                    return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
                }

                // Check if room already exists
                Room room = inventoryService.getRoom(principal.getName(), request.getLocation(), request.getRoom());
                if (room != null) {
                    return new UpdateResponse(UpdateStatus.STORAGE_ALREADY_EXISTS);
                }

                inventoryService.addRoom(new Room()
                        .withName(request.getRoom())
                        .withDescription(" ")
                        .withUsername(principal.getName())
                        .withID(UUID.randomUUID().toString()), location.getID());
                return new UpdateResponse(UpdateStatus.ADDED_ROOM);
            }
            case "REMOVE": {
                final Room room = inventoryService.getRoom(principal.getName(), request.getLocation(), request.getRoom());
                if (room == null) {
                    return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
                }
                inventoryService.removeRoom(room, request.getLocation());
                return new UpdateResponse(UpdateStatus.REMOVED_ROOM);
            }
        }

        return new UpdateResponse(UpdateStatus.UNKNOWN_ERROR);
    }

    @RequestMapping(value="/cabinet", method=POST)
    public @ResponseBody UpdateResponse updateCabinet(@RequestBody final UpdateRequest request, final Principal principal) {

        final String requestType = request.getRequest();

        if (invalidRequest(requestType) && !requestType.toUpperCase().equals("FORK")) {
            return new UpdateResponse((UpdateStatus.INVALID_REQUEST_TYPE));
        }

        if (missingStorageField(request)) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        }

        final Room room = inventoryService.getRoom(principal.getName(), request.getLocation(), request.getRoom());
        if (room == null) {
            return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
        }

        switch (requestType.toUpperCase()) {
            case "ADD": {

                // Check if cabinet already exists
                Cabinet cabinet = inventoryService.getCabinet(principal.getName(),
                        request.getLocation(), request.getRoom(), request.getCabinet());

                if (cabinet != null) {
                    return new UpdateResponse(UpdateStatus.STORAGE_ALREADY_EXISTS);
                }

                if (missingAuditVersion(request)) {
                    request.setAuditVersion("version 1");
                }

                inventoryService.addCabinet(new Cabinet()
                        .withName(request.getCabinet())
                        .withUsername(principal.getName())
                        .withDescription(" ")
                        .withID(UUID.randomUUID().toString())
                        .withAuditVersion(request.getAuditVersion()), room.getID());
                return new UpdateResponse(UpdateStatus.ADDED_CABINET);
            }
            case "REMOVE": {
                final Cabinet cabinet = inventoryService.getCabinet(
                        principal.getName(), request.getLocation(), request.getRoom(), request.getCabinet());
                if (cabinet == null) {
                    return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
                }
                inventoryService.removeCabinet(cabinet, room.getID());
                return new UpdateResponse(UpdateStatus.REMOVED_CABINET);
            }
            case "FORK": {
                if (missingForkVersion(request)) {
                    return new UpdateResponse(UpdateStatus.MISSING_FORK_VERSION);
                }

                final Cabinet cabinet = inventoryService.getCabinet(
                        principal.getName(), request.getLocation(), request.getRoom(), request.getCabinet());

                if (cabinet == null) {
                    return new UpdateResponse(UpdateStatus.INVALID_STORAGE);
                }

                // Check if version exists
                if (cabinet.getAuditVersion(request.getForkVersion()) != null) {
                    return new UpdateResponse(UpdateStatus.FORK_VERSION_ALREADY_EXISTS);
                }

                System.out.println("FORK WITH = " + request.isForkWithChemicals());

                inventoryService.forkCabinet(cabinet, request.getForkVersion(), request.isForkWithChemicals());
                return new UpdateResponse(UpdateStatus.FORKED_CABINET);
            }
        }

        return new UpdateResponse(UpdateStatus.UNKNOWN_ERROR);
    }

    @RequestMapping(value="/chemical", method=POST)
    public @ResponseBody UpdateResponse updateChemical(@RequestBody final UpdateRequest request, final Principal principal) {

        final String requestType = request.getRequest();

        if (invalidRequest(requestType)) {
            return new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        }

        if (missingStorageField(request)) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        }

        final Chemical chemical = inventoryService.getChemical(request.getChemical());

        if (chemical == null) {
            return new UpdateResponse(UpdateStatus.INVALID_CHEMICAL);
        }

        final Cabinet cabinet = inventoryService.getCabinet(principal.getName(),
                request.getLocation(), request.getRoom(), request.getCabinet());

        switch (requestType.toUpperCase()) {

            case "ADD": {
                if (missingAuditVersion(request)) {
                    cabinet.addChemical(request.getChemical(), Chemical.PLACEHOLDER_IMAGE_URL);
                } else {
                    cabinet.addChemical(request.getAuditVersion(), request.getChemical(), Chemical.PLACEHOLDER_IMAGE_URL); // TODO: add image URL
                }
                inventoryService.updateCabinet(cabinet);
                return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
            }
            case "REMOVE": {
                if (missingAuditVersion(request)) {
                    cabinet.removeChemical(request.getChemical());
                } else {
                    cabinet.removeChemical(request.getAuditVersion(), request.getChemical());

                }
                inventoryService.updateCabinet(cabinet);
                return new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
            }
        }

        return new UpdateResponse(UpdateStatus.UNKNOWN_ERROR);
    }

    private boolean invalidRequest(final String request) {
        return request == null || !request.toUpperCase().equals("ADD") && !request.toUpperCase().equals("REMOVE");
    }

    private boolean missingStorageField(final UpdateRequest request) {

        return request == null ||
                request.getLocation() == null ||
                request.getRoom() == null ||
                request.getCabinet() == null ||
                request.getLocation().isEmpty() ||
                request.getRoom().isEmpty()     ||
                request.getCabinet().isEmpty();
    }

    private boolean missingLocation(final UpdateRequest request) {
        return request == null ||
                request.getLocation() == null ||
                request.getLocation().isEmpty();
    }

    private boolean missingLocationOrRoom(final UpdateRequest request) {
        return request == null ||
                request.getLocation() == null ||
                request.getRoom() == null ||
                request.getLocation().isEmpty() ||
                request.getRoom().isEmpty();
    }

    private boolean missingAuditVersion(final UpdateRequest request) {
        return request == null || request.getAuditVersion() == null || request.getAuditVersion().isEmpty();
    }

    private boolean missingForkVersion(final UpdateRequest request) {
        return request == null || request.getForkVersion() == null || request.getForkVersion().isEmpty();
    }
}

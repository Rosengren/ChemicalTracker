package com.chemicaltracker.controller;

import com.chemicaltracker.controller.api.AbstractRestController;
import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.ImageService;
import com.chemicaltracker.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CabinetRestController extends AbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabinetRestController.class);


    public CabinetRestController(ImageService imageService, InventoryService inventoryService) {
        super(imageService, inventoryService);
    }

    @RequestMapping(value = "/add/cabinet", method = POST)
    public ResponseEntity addCabinetHandler(final Principal principal,
                                            @RequestParam("name") final String name,
                                            @RequestParam("description") final String description,
                                            @RequestParam("location") final String location,
                                            @RequestParam("parentID") final String parentID,
                                            @RequestParam("auditVersion") final String auditVersion,
                                            @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + File.separator + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final String uuid = UUID.randomUUID().toString();

                final Room room = inventoryService.getRoom(principal.getName(), parentID);
                room.addStoredItem(name, uuid);
                inventoryService.updateRoom(room);

                final Cabinet cabinet = new Cabinet()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(uuid)
                        .withAuditVersion(auditVersion)
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename);

                inventoryService.addCabinet(cabinet, parentID);

                return new ResponseEntity<>(cabinet, HttpStatus.OK);
            } catch (Exception e) {
                LOGGER.error("An error occurred while adding the cabinet name " + name, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/remove/cabinet/{id}/from/{parentID}", method = GET)
    public ResponseEntity<String> removeCabinetHandler(final Principal principal,
                                                       @PathVariable("id") final String id,
                                                       @PathVariable("parentID") final String parentID) {
        inventoryService.removeCabinet(
                inventoryService.getCabinet(principal.getName(), id), parentID);
        // TODO: remove image
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/cabinet/image", method = POST)
    public ResponseEntity updateCabinetImageHandler(final Principal principal,
                                                    @RequestParam("location") final String location,
                                                    @RequestParam("room") final String room,
                                                    @RequestParam("cabinet") final String cabinetName,
                                                    @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {

            final Cabinet cabinet = inventoryService.getCabinet(principal.getName(), location, room, cabinetName);

            final String imageName = cabinetName.toLowerCase().replace(' ', '-') + IMAGE_EXTENSION;

            final String filename = principal.getName() + File.separator + location
                    + File.separator + room + File.separator + cabinetName + File.separator + imageName;

            try {
                imageService.add(image, filename, imageName);
                cabinet.setImageURL(S3_BASE_URL + filename);

                inventoryService.updateCabinet(cabinet);
                return new ResponseEntity<>(room, HttpStatus.OK);
            } catch (Exception e) {
                LOGGER.error("An error occurred while adding the image to cabinet: " + cabinetName, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("The image file was missing", HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/update/cabinet", method=POST)
    public UpdateResponse updateCabinet(@RequestBody final UpdateRequest request, final Principal principal) {

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
}

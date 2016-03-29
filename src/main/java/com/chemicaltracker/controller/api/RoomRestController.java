package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.model.Location;
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
public class RoomRestController extends AbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomRestController.class);

    public RoomRestController(ImageService imageService, InventoryService inventoryService) {
        super(imageService, inventoryService);
    }

    @RequestMapping(value = "/add/room", method = POST)
    public ResponseEntity addRoomHandler(final Principal principal,
                                         @RequestParam("name") final String name,
                                         @RequestParam("description") final String description,
                                         @RequestParam("location") final String location,
                                         @RequestParam("parentID") final String parentID,
                                         @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + File.separator + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final Room room = new Room()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(UUID.randomUUID().toString())
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename);

                inventoryService.addRoom(room, parentID);

                return new ResponseEntity<>(room, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/remove/room/{id}/from/{parentID}", method = GET)
    public ResponseEntity<String> removeRoomHandler(final Principal principal,
                                                    @PathVariable("id") final String id,
                                                    @PathVariable("parentID") final String parentID) {
        inventoryService.removeRoom(
                inventoryService.getRoom(principal.getName(), id), parentID);
        // TODO: remove image
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/room/image", method = POST)
    public ResponseEntity updateRoomImageHandler(final Principal principal,
                                                 @RequestParam("location") final String location,
                                                 @RequestParam("room") final String roomName,
                                                 @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {

            final Room room = inventoryService.getRoom(principal.getName(), location, roomName);

            final String imageName = roomName.toLowerCase().replace(' ', '-') + IMAGE_EXTENSION;

            final String filename = principal.getName() + File.separator + location
                    + File.separator + roomName + File.separator + imageName;

            try {
                imageService.add(image, filename, imageName);
                room.setImageURL(S3_BASE_URL + filename);

                inventoryService.updateRoom(room);
                return new ResponseEntity<>(room, HttpStatus.OK);
            } catch (Exception e) {
                LOGGER.error("An error occurred while adding the image to room: " + roomName, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("The image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/update/room", method=POST)
    public @ResponseBody
    UpdateResponse updateRoom(@RequestBody final UpdateRequest request, final Principal principal) {

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

}

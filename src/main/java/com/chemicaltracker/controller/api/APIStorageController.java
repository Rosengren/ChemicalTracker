package com.chemicaltracker.controller.api;

import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.ImageService;
import com.chemicaltracker.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.UUID;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Handles Adding and removing locations/rooms/cabinets/chemials from parent object
 */
@Controller
@RequestMapping(value = "/api")
public class APIStorageController {

    private static final Logger logger = LoggerFactory.getLogger(APIStorageController.class);

    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String S3_BASE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/";

    private final ImageService imageService;
    private final InventoryService inventoryService;

    @Autowired
    public APIStorageController(ImageService imageService, InventoryService inventoryService) {
        this.imageService= imageService;
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value = "/add/location", method = POST)
    public ResponseEntity addLocationHandler(final Principal principal,
                                             @RequestParam("name") final String name,
                                             @RequestParam("description") final String description,
                                             @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                final Location location = new Location()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(name) // same as name for location
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename);


                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);
                inventoryService.addLocation(location);

                return new ResponseEntity<>(location, HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error occurred while adding location", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/remove/location/{id}/from/{parentID}", method = GET)
    public ResponseEntity<String> removeLocationHandler(final Principal principal,
                                                        @PathVariable("id") final String id) {
        inventoryService.removeLocation(new Location()
                .withUsername(principal.getName())
                .withID(id));
        // TODO: remove image
        return new ResponseEntity<>(HttpStatus.OK);
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

                final String filename = principal.getName() + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final String uuid = UUID.randomUUID().toString();

                final Location parentLocation = inventoryService.getLocation(principal.getName(), parentID);
                parentLocation.addStoredItem(name, uuid);
                inventoryService.updateLocation(parentLocation);

                final Room room = new Room()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(uuid)
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
        inventoryService.removeRoom(new Room()
                .withUsername(principal.getName())
                .withID(id), parentID);
        // TODO: remove image
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/add/cabinet", method = POST)
    public ResponseEntity addCabinetHandler(final Principal principal,
                                                    @RequestParam("name") final String name,
                                                    @RequestParam("description") final String description,
                                                    @RequestParam("location") final String location,
                                                    @RequestParam("parentID") final String parentID,
                                                    @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final String uuid = UUID.randomUUID().toString();

                final Room room = inventoryService.getRoom(principal.getName(), parentID);
                room.addStoredItem(name, uuid);
                inventoryService.updateRoom(room);

                final Cabinet cabinet = new Cabinet()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(uuid)
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename);

                inventoryService.addCabinet(cabinet, parentID);

                return new ResponseEntity<>(cabinet, HttpStatus.OK);
            } catch (Exception e) {
                logger.error("An error occurred while adding the cabinet name " + name, e);
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
        inventoryService.removeCabinet(new Cabinet()
                .withUsername(principal.getName())
                .withID(id), parentID);
        // TODO: remove image
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

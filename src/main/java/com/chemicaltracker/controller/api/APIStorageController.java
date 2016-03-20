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
import org.springframework.web.bind.annotation.RequestBody;
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
 * Handles adding and removing locations/rooms/cabinets from parent object
 * Note: these are not used by other devices like android/google glass.
 *       See APIUpdateController for these methods
 */
@Controller
@RequestMapping(value = "/api")
public class APIStorageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIStorageController.class);

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
                LOGGER.error("Error occurred while adding location", e);
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

    @RequestMapping(value = "/update/chemicalImage", method = POST)
    public ResponseEntity updateChemicalImageHandler(final Principal principal,
                                                    @RequestParam("name") final String name, // chemical name
                                                    @RequestParam("location") final String location,
                                                    @RequestParam("cabinetID") final String cabinetID,
                                                    @RequestParam("auditVersion") final String auditVersion,
                                                    @RequestParam("image") final MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                final Cabinet cabinet = inventoryService.getCabinet(principal.getName(), cabinetID);

                final String filename = principal.getName() + File.separator + location + File.separator + cabinet.getName()
                        + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                imageService.add(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                cabinet.getAuditVersion(auditVersion).addChemical(name, S3_BASE_URL + filename);
                inventoryService.updateCabinet(cabinet);
                return new ResponseEntity<>(cabinet, HttpStatus.OK);
            } catch (Exception e) {
                LOGGER.error("An error occurred while adding the chemical name " + name, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("The image file was missing", HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/update/location/image", method = POST)
    public ResponseEntity updateCabinetImageHandler(final Principal principal,
                                                    @RequestParam("location") final String locationName,
                                                    @RequestParam("image") final MultipartFile image) {

        if (!image.isEmpty()) {

            final Location location = inventoryService.getLocation(principal.getName(), locationName);

            final String imageName = locationName.toLowerCase().replace(' ', '-') + IMAGE_EXTENSION;

            final String filename = principal.getName() + File.separator + location
                    + File.separator + imageName;

            try {
                imageService.add(image, filename, imageName);
                location.setImageURL(S3_BASE_URL + filename);
                inventoryService.updateLocation(location);
                return new ResponseEntity<>(location, HttpStatus.OK);
            } catch (Exception e) {
                LOGGER.error("An error occurred while adding the location image to location: " + locationName, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("The image file was missing", HttpStatus.BAD_REQUEST);
        }
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
                LOGGER.error("An error occured while adding the image to room: " + roomName, e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("The image file was missing", HttpStatus.BAD_REQUEST);
        }
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



}

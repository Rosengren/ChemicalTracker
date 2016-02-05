package com.chemicaltracker.controller;

import com.chemicaltracker.model.*;
import com.chemicaltracker.persistence.*;

import java.util.UUID;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.ui.Model;
import java.security.Principal;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    // TODO: add support for other file types
    private static final String IMAGE_EXTENSION = ".jpg";

    private static final String S3_BASE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/";

    private static final ImageDAO imageDB = ImageDAO.getInstance();
    private static final LocationDAO locationDB = LocationDAO.getInstance();
    private static final CabinetDAO cabinetDB = CabinetDAO.getInstance();
    private static final RoomDAO roomDB = RoomDAO.getInstance();

    @RequestMapping(value = "/add/location", method = POST)
    public ResponseEntity addLocationHandler(Principal principal,
            @RequestParam("Name") String name,
            @RequestParam("Description") String description,
            @RequestParam("Location") String location,
            @RequestParam("ParentID") String paretnID,
            @RequestParam("Image") MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                uploadStorageImage(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                locationDB.create(
                    new Location()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(name) // same as name for location
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename));

                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error occurred while adding location", e);
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/add/room", method = POST)
    public ResponseEntity addRoomHandler(Principal principal,
            @RequestParam("Name") String name,
            @RequestParam("Description") String description,
            @RequestParam("Location") String location,
            @RequestParam("ParentID") String parentID,
            @RequestParam("Image") MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                uploadStorageImage(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final String uuid = UUID.randomUUID().toString();

                final Location parentLocation = locationDB.find(principal.getName(), parentID);
                parentLocation.addStoredItem(name, uuid);
                locationDB.update(parentLocation);

                roomDB.create(
                    new Room()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(uuid)
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename));

                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/add/cabinet", method = POST)
    public ResponseEntity addCabinetHandler(Principal principal,
            @RequestParam("Name") String name,
            @RequestParam("Description") String description,
            @RequestParam("Location") String location,
            @RequestParam("ParentID") String parentID,
            @RequestParam("Image") MultipartFile image) {

        if (!image.isEmpty()) {
            try {

                final String filename = principal.getName() + location + File.separator + name.replace(' ', '-') + IMAGE_EXTENSION;

                uploadStorageImage(image, filename, name.replace(' ', '-') + IMAGE_EXTENSION);

                final String uuid = UUID.randomUUID().toString();

                final Room room = roomDB.find(principal.getName(), parentID);
                room.addStoredItem(name, uuid);
                roomDB.update(room);

                cabinetDB.create(
                    new Cabinet()
                        .withUsername(principal.getName())
                        .withName(name)
                        .withID(uuid)
                        .withDescription(description)
                        .withImageURL(S3_BASE_URL + filename));

                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }

    private void uploadStorageImage(final MultipartFile image, final String filename, final String name) throws Exception {

        if (image.isEmpty()) {
            logger.warn("The image file was missing");
            throw new Exception("Empty image file");
        }

        byte[] bytes = image.getBytes();

        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "tmpImages");
        if (!dir.exists())
            dir.mkdirs();

        // Create the image on server
        File serverFile = new File(dir.getAbsolutePath()
                + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("Server Image Location="
                + serverFile.getAbsolutePath());

        imageDB.uploadImage(serverFile.getAbsolutePath(), filename);
    }

}

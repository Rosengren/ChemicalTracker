package com.chemicaltracker.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.chemicaltracker.persistence.ImageDataAccessObject;
import com.chemicaltracker.persistence.ImageDataAccessS3;

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

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    // TODO: add support for other file types
    private static final String IMAGE_EXTENSION = ".jpg";

    private static final ImageDataAccessObject imageDB = new ImageDataAccessS3();

    // TODO: rename this and move it a different controller or change the controller name
    // since this will be used for submitting the complete form and not just the image
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity uploadFileHandler(Principal principal,
            @RequestParam("Name") String name,
            @RequestParam("Description") String description,
            @RequestParam("Location") String location,
            @RequestParam("Image") MultipartFile image) {

        System.out.println("Name: " + name);
        System.out.println("Desc: " + description);
        System.out.println("Location: " + location);

        if (!image.isEmpty()) {
            try {
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

                imageDB.uploadImage(serverFile.getAbsolutePath(),
                        principal.getName() + location +
                        File.separator + name.replace(' ', '-') + IMAGE_EXTENSION);

                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("the image file was missing", HttpStatus.BAD_REQUEST);
        }
    }
}

package com.chemicaltracker.controller;

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

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    /**
     * Upload single file using Spring Controller
     */
    // TODO: rename this and move it a different controller or change the controller name
    // since this will be used for submitting the complete form and not just the image
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity uploadFileHandler(@RequestParam("Name") String name,
            @RequestParam("Description") String description,
            @RequestParam("Image") MultipartFile image) {

        // TODO: return a message if failure such as a missing 

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

                return new ResponseEntity(HttpStatus.OK); //"success";
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST); //"failed"; // REASON: You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST); //"failed"; // REASON: "You failed to upload " + name
                    //+ " because the file was empty.";
        }
    }
}

package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.model.Location;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class LocationRestController extends AbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestController.class);

    public LocationRestController(ImageService imageService, InventoryService inventoryService) {
        super(imageService, inventoryService);
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


    @RequestMapping(value="/update/location", method=POST)
    public @ResponseBody
    UpdateResponse updateLocation(@RequestBody final UpdateRequest request, final Principal principal) {

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


}

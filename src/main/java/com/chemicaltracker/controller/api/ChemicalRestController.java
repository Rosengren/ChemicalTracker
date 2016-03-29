package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.service.ImageService;
import com.chemicaltracker.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.AbstractController;

import java.io.File;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ChemicalRestController extends AbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChemicalRestController.class);

    public ChemicalRestController(ImageService imageService, InventoryService inventoryService) {
        super(imageService, inventoryService);
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


    @RequestMapping(value="/update/chemical", method=POST)
    public @ResponseBody
    UpdateResponse updateChemical(@RequestBody final UpdateRequest request, final Principal principal) {

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


}

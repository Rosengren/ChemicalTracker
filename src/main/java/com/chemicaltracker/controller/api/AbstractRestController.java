package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.service.ImageService;
import com.chemicaltracker.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public abstract class AbstractRestController {

    // TODO: move these to config file or Constants Object
    protected static final String IMAGE_EXTENSION = ".jpg";
    protected static final String S3_BASE_URL = "https://s3-us-west-2.amazonaws.com/chemical-images/";

    protected final ImageService imageService;
    protected final InventoryService inventoryService;

    @Autowired
    public AbstractRestController(ImageService imageService, InventoryService inventoryService) {
        this.imageService= imageService;
        this.inventoryService = inventoryService;
    }

    protected boolean invalidRequest(final String request) {
        return request == null || !request.toUpperCase().equals("ADD") && !request.toUpperCase().equals("REMOVE");
    }

    protected boolean missingStorageField(final UpdateRequest request) {

        return request == null ||
                request.getLocation() == null ||
                request.getRoom() == null ||
                request.getCabinet() == null ||
                request.getLocation().isEmpty() ||
                request.getRoom().isEmpty()     ||
                request.getCabinet().isEmpty();
    }

    protected boolean missingLocation(final UpdateRequest request) {
        return request == null ||
                request.getLocation() == null ||
                request.getLocation().isEmpty();
    }

    protected boolean missingLocationOrRoom(final UpdateRequest request) {
        return request == null ||
                request.getLocation() == null ||
                request.getRoom() == null ||
                request.getLocation().isEmpty() ||
                request.getRoom().isEmpty();
    }

    protected boolean missingAuditVersion(final UpdateRequest request) {
        return request == null || request.getAuditVersion() == null || request.getAuditVersion().isEmpty();
    }

    protected boolean missingForkVersion(final UpdateRequest request) {
        return request == null || request.getForkVersion() == null || request.getForkVersion().isEmpty();
    }
}

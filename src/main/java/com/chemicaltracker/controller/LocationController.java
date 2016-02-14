package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.InventoryService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;


// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class LocationController {

    private final InventoryService inventoryService;

    @Autowired
    public LocationController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/{locationName}")
    public ModelAndView viewLocation(@PathVariable("locationName") final String locationName, final Model model, final Principal principal) {

        final List<Room> rooms = inventoryService.getRooms(principal.getName(), locationName);

        final ModelAndView locationView = new ModelAndView("location");
        locationView.addObject("rooms", rooms);
        locationView.addObject("title", locationName);
        locationView.addObject("username", principal.getName());
        locationView.addObject("location", locationName);

        return locationView;
    }

}


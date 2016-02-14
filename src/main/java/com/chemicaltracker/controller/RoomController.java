package com.chemicaltracker.controller;

import com.chemicaltracker.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class RoomController {

    private final InventoryService inventoryService;

    @Autowired
    public RoomController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/{locationName}/{roomName}")
    public ModelAndView viewRoom(@PathVariable("locationName") final String locationName,
                                     @PathVariable("roomName") final String roomName,
                                     final Principal principal) {

        final ModelAndView roomView = new ModelAndView("room");

        roomView.addObject("title", roomName);
        roomView.addObject("username", principal.getName());
        roomView.addObject("cabinets", inventoryService.getCabinets(principal.getName(), locationName, roomName));
        roomView.addObject("addURL", "/add/cabinet/");
        roomView.addObject("location", locationName);

        return roomView;
    }
}

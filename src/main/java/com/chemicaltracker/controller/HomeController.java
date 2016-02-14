package com.chemicaltracker.controller;

import java.security.Principal;
import java.util.Arrays;
import org.springframework.web.servlet.ModelAndView;
import com.chemicaltracker.service.InventoryService;

// Annotations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private static final String TITLE = "Locations";

    private final InventoryService inventoryService;

    @Autowired
    public HomeController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value = {"/home", "/Home"})
    public ModelAndView home(final Principal principal) {

        final ModelAndView home = new ModelAndView("home");
        home.addObject("title", TITLE);
        home.addObject("subtitle", "All of your locations");
        home.addObject("username", principal.getName());
        home.addObject("locations", inventoryService.getLocations(principal.getName()));
        home.addObject("addURL", "/add/location");
        home.addObject("breadcrumbs", Arrays.asList("Home"));

        return home;
    }
}

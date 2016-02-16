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
@RequestMapping(value = {"/home", "/Home"})
public class HomeController {

    private final InventoryService inventoryService;

    @Autowired
    public HomeController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @RequestMapping(value="")
    public ModelAndView home(final Principal principal) {

        final ModelAndView home = new ModelAndView("home");
        home.addObject("locations", inventoryService.getLocations(principal.getName()));
        return home;
    }
}

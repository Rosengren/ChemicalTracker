package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.service.InventoryService;
import org.springframework.web.servlet.ModelAndView;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class ChemicalController {

    private final InventoryService inventoryService;

    @Autowired
    public ChemicalController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value = "/{locationName}/{roomName}/{cabinetName}/{chemicalName}")
    public ModelAndView viewChemical(
            @PathVariable("locationName") final String locationName,
            @PathVariable("roomName")     final String roomName,
            @PathVariable("cabinetName")  final String cabinetName,
            @PathVariable("chemicalName") final String chemicalName,
            final Principal principal) {

        final String username = principal.getName();
        final Chemical chemical = inventoryService.getChemical(chemicalName);
        final ModelAndView chemicalView = new ModelAndView("chemical");

        chemicalView.addObject("username", username);
        chemicalView.addObject("chemical", chemical);
        chemicalView.addObject("fireDiamond", chemical.getFireDiamond());
        chemicalView.addObject("location", locationName);
        chemicalView.addObject("room", roomName);
        chemicalView.addObject("cabinet", cabinetName);
        chemicalView.addObject("title", chemicalName);

        return chemicalView;
    }
}

package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Chemical;
import org.springframework.web.servlet.ModelAndView;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class ChemicalController {

    private static final ChemicalDao chemicalsDB = ChemicalDao.getInstance();

    @RequestMapping(value = "/{locationName}/{roomName}/{cabinetName}/{chemicalName}")
    public ModelAndView viewChemical(
            @PathVariable("locationName") final String locationName,
            @PathVariable("roomName")     final String roomName,
            @PathVariable("cabinetName")  final String cabinetName,
            @PathVariable("chemicalName") final String chemicalName,
            final Principal principal) {

        final ModelAndView chemicalView = new ModelAndView("chemical");

        final String username = principal.getName();
        final Chemical chemical = chemicalsDB.find(chemicalName);

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

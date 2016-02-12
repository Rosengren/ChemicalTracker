package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Location;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class LocationController {

    private static final LocationDao locationsDB = LocationDao.getInstance();
    private static final RoomDao roomsDB = RoomDao.getInstance();

    @RequestMapping("/{locationName}")
    public ModelAndView viewLocation(@PathVariable("locationName") final String locationName, final Model model, final Principal principal) {

        final Location location = locationsDB.find(principal.getName(), locationName);
        final List<String> roomIDs = location.getStoredItemIDs();

        final ModelAndView locationView = new ModelAndView("location");
        locationView.addObject("rooms", roomsDB.findAllByIds(principal.getName(), roomIDs));
        locationView.addObject("title", locationName);
        locationView.addObject("username", principal.getName());
        locationView.addObject("parentID", location.getID());
        locationView.addObject("location", locationName);

        return locationView;
    }

}


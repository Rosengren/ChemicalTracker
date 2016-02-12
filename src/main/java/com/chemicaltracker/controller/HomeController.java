package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.LocationDao;
import java.security.Principal;
import java.util.Arrays;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String TITLE = "Locations";

    private static final LocationDao locationDB = LocationDao.getInstance();

    @RequestMapping(value = {"/home", "/Home"})
    public ModelAndView home(final Principal principal) {

        ModelAndView home = new ModelAndView("home");
        home.addObject("title", TITLE);
        home.addObject("subtitle", "All of your locations");
        home.addObject("username", principal.getName());
        home.addObject("locations", locationDB.findAll(principal.getName()));
        home.addObject("addURL", "/add/location");
        home.addObject("breadcrumbs", Arrays.asList("Home"));

        return home;
    }
}

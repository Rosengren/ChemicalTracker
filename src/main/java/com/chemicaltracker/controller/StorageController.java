package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class StorageController {

    private StorageDataAccessObject cabinetDB = StorageFactory.getStorage("CABINETS");

    @RequestMapping(value={"/home", "/locations"})
    public String home(Model model, Principal principal) {
        //model.addAttribute("username", principal.getName());
        //model.addAttribute("cabinets", cabinetDB.getAllStoragesForUser(principal.getName()));
        model.addAttribute("title", "Locations");
        model.addAttribute("subtitle", "List of all your locations");
        model.addAttribute("linkToGoTo", "/rooms");
        return "storageView";
    }

    @RequestMapping("/rooms")
    public String rooms(Model model, Principal principal) {
        model.addAttribute("title", "Rooms");
        model.addAttribute("subtitle", "List of all the rooms in [add location here]");
        model.addAttribute("linkToGoTo", "/cabinets");
        return "storageView";
    }

    @RequestMapping("/cabinets")
    public String cabinets(Model model, Principal principal) {
        model.addAttribute("title", "Cabinets");
        model.addAttribute("subtitle", "List of all the cabinets in room [add room here]");
        model.addAttribute("linkToGoTo", "/chemicals");
        return "storageView";
    }

    @RequestMapping("/chemicals")
    public String chemicals(Model model, Principal principal) {
        model.addAttribute("title", "Chemicals");
        model.addAttribute("subtitle", "List of all the chemicals in cabinet [add cabinet here]");
        model.addAttribute("linkToGoTo", "/chemical");
        return "storageView";
    }

}

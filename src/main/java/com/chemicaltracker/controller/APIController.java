package com.chemicaltracker.controller;

import java.util.List;
//import org.json.simple.JSONObject;

import com.chemicaltracker.model.Storage;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;

import org.springframework.ui.Model;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api")
public class APIController {

    private StorageDataAccessObject locationDB = StorageFactory.getStorage("LOCATIONS");
    private StorageDataAccessObject roomDB = StorageFactory.getStorage("ROOMS");
    private StorageDataAccessObject cabinetDB = StorageFactory.getStorage("CABINETS");

    private ChemicalDataAccessObject chemicalDB = new ChemicalDataAccessDynamoDB();

    // TODO: consider getting all locations

    // FIXME: need to add principal once we know it works
    // TODO: add star (*) to indicate "Get all Locations" (Object of Objects)
    @RequestMapping(value = "/user/{username}/location/{locationName}", method=GET)
    public @ResponseBody String getRoomsForLocation(@PathVariable("username") String username, @PathVariable("locationName") String locationName, Model model) {
        Storage location = locationDB.getStorage(username, locationName);
        return location.toJSON();
    }

    //@RequestMapping(value = "/user/{username}/location/{locationName}/Room/{roomName}", method=GET)
    //public @RequestBody String getCabinetsForRoom(@PathVariable("username") String username, @PathVariable("locationName") String locationName,
            //@PathVariable("roomName") String roomName, Model model) {
        //Storage location = locationDB.getStorage(username, locationName);

    //}





     //CLEAN UP //

    //@RequestMapping(value = "/new", method=GET)
    //public String initCabinetForm(Model model, Principal principal) {
        //model = addAttributes(model, principal, new Storage());
        //return "cabinets/createCabinet";
    //}

    //@RequestMapping(value = "/new", method=POST)
    //public String processChemicalForm(@ModelAttribute Storage cabinet,
            //BindingResult result, Model model, Principal principal) {

        //model = addAttributes(model, principal, cabinet);

        //if (result.hasErrors()) {
            //model.addAttribute("success", false);
        //} else {
            //model.addAttribute("success", true);
            //cabinetDB.addStorage(cabinet);
        //}

        //return "cabinets/createCabinet";
    //}

    //private Model addAttributes(Model model, Principal principal, Storage cabinet) {
        //model.addAttribute("cabinet", cabinet);
        //model.addAttribute("username", principal.getName());

        //List<String> chemicalNames = cabinet.getStoredItemNames();
        //if (chemicalNames.isEmpty()) {
            //model.addAttribute("chemicals", chemicalDB.getAllChemicals());
        //} else {
            //model.addAttribute("chemicals", chemicalDB.batchGetChemicals(chemicalNames));
        //}
        //return model;
    //}

    //@RequestMapping(value="/view/{cabinetName}", method=GET)
    //public String viewCabinet(@PathVariable("cabinetName") String cabinetName, Model model, Principal principal) {
        //Storage cabinet = cabinetDB.getStorage(principal.getName(), cabinetName);
        //model = addAttributes(model, principal, cabinet);
        //return "cabinets/viewCabinet";
    //}

    //@RequestMapping(value="/delete/{cabinetName}", method=GET)
    //public String deleteCabinet(@PathVariable("cabinetName") String cabinetName, Model model, Principal principal) {
         //TODO
        //return "";
    //}
}

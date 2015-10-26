package com.chemicaltracker.controller;

import com.chemicaltracker.model.Chemical;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ChemicalController {

    private ChemicalDataAccessObject database = new ChemicalDataAccessDynamoDB();

    @RequestMapping(path="/chemical", method=GET)
    public Chemical chemical(@RequestParam(value="name") String name) {
        return database.getChemical(name);
    }

    //@RequestMapping(path="/chemical", method=RequestMethod.POST)
    //public String chemical(@Valid Chemical chemial, BindingResult result) {
        //if (result.hasErrors()) {
            //return "/"; // TODO: HANDLE ERROR
        //}

        //database.addChemical(chemical);
        //return "/"; // TODO: REDIRECT
    //}
}

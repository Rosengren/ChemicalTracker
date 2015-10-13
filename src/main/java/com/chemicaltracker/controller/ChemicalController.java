package com.chemicaltracker.controller;

import com.chemicaltracker.model.Chemical;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChemicalController {

    private ChemicalDataAccessObject database = new ChemicalDataAccessDynamoDB();

    @RequestMapping("/chemical")
    public Chemical chemical(@RequestParam(value="name") String name) {
        return database.getChemical(name);
    }
}

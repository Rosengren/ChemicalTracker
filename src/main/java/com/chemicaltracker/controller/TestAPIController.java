package com.chemicaltracker.controller;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.UpdateRequest;
import com.chemicaltracker.model.ChemicalQueryRequest;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api/test")
public class TestAPIController {

    private ChemicalDataAccessObject chemicalDB = ChemicalDataAccessDynamoDB.getInstance();

    @RequestMapping(value="/update", method=POST)
    public @ResponseBody String testUpdate(@RequestBody final UpdateRequest request, BindingResult result,
            Model model, Principal principal) {

        return request.toJSONString();
    }

    @RequestMapping(value="/query", method=POST)
    public @ResponseBody String testQuery(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final Chemical chemical = chemicalDB.getChemical(request.getChemical());
        return chemical.toJSONString();
    }
}

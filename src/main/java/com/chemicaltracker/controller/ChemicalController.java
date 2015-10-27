package com.chemicaltracker.controller;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.FireDiamond;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

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
@RequestMapping("/chemicals")
public class ChemicalController {

    private ChemicalDataAccessObject chemicalDB = new ChemicalDataAccessDynamoDB();

    @RequestMapping(value = "/new", method=GET)
    public String initChemicalForm(Model model) {
        model.addAttribute("chemical", new Chemical());
        return "chemicals/addChemical";
    }

    @RequestMapping(value = "/new", method=POST)
    public String processChemicalForm(@ModelAttribute Chemical chemical, BindingResult result, Model model) {
        model.addAttribute("chemical", chemical);
        if (result.hasErrors()) {
            model.addAttribute("success", false);
        } else {
            model.addAttribute("success", true);
            chemicalDB.addChemical(chemical);
        }

        return "chemicals/addChemical";
    }

    @RequestMapping(value="/view/{chemicalName}", method=GET)
    public String viewChemical(@PathVariable("chemicalName") String chemicalName, Model model, Principal principal) {
        Chemical chemical = chemicalDB.getChemical(chemicalName);
        model.addAttribute("chemical", chemical);
        model.addAttribute("fireDiamond", chemical.getFireDiamond());
        return "chemicals/viewChemical";
    }



    //@RequestMapping(value="/addChemical", method=POST)
    //public String addChemical(Model model) {
        //System.out.println(model.toString());
        //return "addChemical";
    //}

    //@RequestMapping(value="/addChemical")
    //public String addChemicalForm(Model model) {
        //return "addChemical";
    //}

    //@RequestMapping(value="/chemical", method=GET)
    //public Chemical chemical(@RequestParam(value="name") String name) {
        //return chemicalDB.getChemical(name);
    //}

    //@RequestMapping(value="/chemical")
    //public String showChemical(Model model) {
        //return "showChemical";
    //}
}

package com.chemicaltracker.controller;

import com.chemicaltracker.model.Container;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import com.chemicaltracker.persistence.ContainerDataAccessObject;
import com.chemicaltracker.persistence.ContainerDataAccessDynamoDB;

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
@RequestMapping("/containers")
public class ContainerController {

    private ContainerDataAccessObject containerDB = new ContainerDataAccessDynamoDB();
    private ChemicalDataAccessObject chemicalDB = new ChemicalDataAccessDynamoDB();

    @RequestMapping(value = "/new", method=GET)
    public String initContainerForm(Model model, Principal principal) {
        model = addAttributes(model, principal, new Container());
        return "containers/createContainer";
    }

    @RequestMapping(value = "/new", method=POST)
    public String processChemicalForm(@ModelAttribute Container container, 
            BindingResult result, Model model, Principal principal) {

        model = addAttributes(model, principal, container);

        if (result.hasErrors()) {
            model.addAttribute("success", false);
        } else {
            model.addAttribute("success", true);
            containerDB.addContainer(container);
        }

        return "containers/createContainer";
    }

    private Model addAttributes(Model model, Principal principal, Container container) {
        model.addAttribute("container", container);
        model.addAttribute("username", principal.getName());

        if (container.getChemicalNames().isEmpty()) {
            model.addAttribute("chemicals", chemicalDB.getAllChemicals());
        } else {
            model.addAttribute("chemicals", chemicalDB.batchGetChemicals(container.getChemicalNames()));
        }
        return model;
    }

    @RequestMapping(value="/view/{containerName}", method=GET)
    public String viewContainer(@PathVariable("containerName") String containerName, Model model, Principal principal) {
        Container container = containerDB.getContainer(principal.getName(), containerName);
        model = addAttributes(model, principal, container);
        return "containers/viewContainer";
    }

    @RequestMapping(value="/delete/{containerName}", method=GET)
    public String deleteContainer(@PathVariable("containerName") String containerName, Model model, Principal principal) {
        return "";
    }
}

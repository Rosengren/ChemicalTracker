package com.chemicaltracker.controller;

import com.chemicaltracker.model.Chemical;
import com.chemicaltracker.model.UpdateStatus;
import com.chemicaltracker.model.UpdateRequest;
import com.chemicaltracker.model.UpdateResponse;
import com.chemicaltracker.model.ChemicalQueryRequest;

import com.chemicaltracker.persistence.ChemicalDAO;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDAO;

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

    private ChemicalDAO chemicalDB = ChemicalDAO.getInstance();
    
    @RequestMapping(value="/success")
    public @ResponseBody String success() {
        return "success";
    }

    @RequestMapping(value="/update", method=POST)
    public @ResponseBody String testUpdate(@RequestBody final UpdateRequest request, BindingResult result,
            Model model, Principal principal) {

        final String requestType = request.getRequestType();
        UpdateResponse response = new UpdateResponse();

        //System.out.println(request.toJSONString());

        if (!requestType.equals("ADD") && !requestType.equals("REMOVE")) {
            response = new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        } else if (request.getUsername().equals("invalid") || request.getUsername().equals("")) {
            response = new UpdateResponse(UpdateStatus.INVALID_USERNAME);
        } else if (request.getLocation().equals("invalid") || request.getLocation().equals("") ||
                    request.getRoom().equals("invalid") || request.getRoom().equals("") ||
                    request.getCabinet().equals("invalid") || request.getCabinet().equals("")) {
            response = new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        } else {
            if (request.getChemical().equals("valid")) {
                if (requestType.equals("ADD")) {
                    response = new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
                } else {
                    response = new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
                }
            } else {
                final Chemical chemical = chemicalDB.getChemical(request.getChemical());
                if (chemical.getName().equals("")) {
                    response = new UpdateResponse(UpdateStatus.INVALID_CHEMICAL);
                } else {
                    if (requestType.equals("ADD")) {
                        response = new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
                    } else {
                        response = new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
                    }
                }
            }
        }

        return response.toJSONString();
    }

    @RequestMapping(value="/query", method=POST)
    public @ResponseBody String testQuery(@RequestBody final ChemicalQueryRequest request, BindingResult result,
            Model model, Principal principal) {

        final Chemical chemical = chemicalDB.getChemical(request.getChemical());
        return chemical.toJSONString();
    }
}

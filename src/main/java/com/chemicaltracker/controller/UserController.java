package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.persistence.ContainerDataAccessObject;
import com.chemicaltracker.persistence.ContainerDataAccessDynamoDB;

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
public class UserController {

    private ContainerDataAccessObject containerDB = new ContainerDataAccessDynamoDB();

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("containers", containerDB.getAllContainersForUser(principal.getName()));
        return "home";
    }
}

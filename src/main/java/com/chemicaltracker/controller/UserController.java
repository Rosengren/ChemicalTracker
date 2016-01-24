package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.persistence.UserDAO;
import com.chemicaltracker.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    private UserDAO userDB = UserDAO.getInstance();

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @RequestMapping(value="/signup", method=POST)
    public String signupPost(@RequestBody final User user, Model model) {
        userDB.createUser(user.getUsername(), user.getPassword(), user.getRole());
        return "welcome";
    }
}

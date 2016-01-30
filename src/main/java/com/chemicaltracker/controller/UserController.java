package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import com.chemicaltracker.persistence.UserDAO;
import com.chemicaltracker.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String login(Principal principal, Model model) {
        if (principal == null) {
            return "login";
        }
        return "welcome";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @RequestMapping(value="/signup", method=POST)
    public String signupPost(@ModelAttribute("userForm") final User user, Model model) {
        userDB.createUser(user.getUsername(), user.getPassword(), user.getRole());
        return "welcome";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "welcome";
    }
}

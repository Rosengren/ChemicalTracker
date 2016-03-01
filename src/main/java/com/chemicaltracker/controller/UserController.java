package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.User;
import com.chemicaltracker.service.UserService;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chemicaltracker.service.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    private final UserService userService = new UserServiceImpl();

    @RequestMapping("/login")
    public ModelAndView login(final Principal principal) {
        if (principal == null) {
            return new ModelAndView("login");
        }

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping("/signup")
    public ModelAndView signUp() {
        return new ModelAndView("signup");
    }

    @RequestMapping(value="/signup", method=POST)
    public ModelAndView signUpPost(@ModelAttribute("userForm") final User user) {
        userService.addUser(user);
        return new ModelAndView("signup");
    }

    @RequestMapping(value="/logout", method=GET)
    public ModelAndView logoutPage(final HttpServletRequest request, final HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ModelAndView("welcome");
    }
}


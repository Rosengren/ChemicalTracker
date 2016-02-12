package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.UserDao;
import com.chemicaltracker.persistence.model.User;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private UserDao userDB = UserDao.getInstance();

    @RequestMapping("/login")
    public ModelAndView login(final Principal principal) {
        if (principal == null) {
            return new ModelAndView("login");
        }

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @RequestMapping(value="/signup", method=POST)
    public ModelAndView signUpPost(@ModelAttribute("userForm") final User user) {
        userDB.create(user);
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


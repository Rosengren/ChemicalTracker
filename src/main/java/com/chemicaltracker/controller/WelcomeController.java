package com.chemicaltracker.controller;


// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {

    @RequestMapping("/")
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }
}

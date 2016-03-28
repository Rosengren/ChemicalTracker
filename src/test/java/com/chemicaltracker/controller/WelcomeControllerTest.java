package com.chemicaltracker.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class WelcomeControllerTest {

    private static final String WELCOME_PAGE = "welcome";

    private WelcomeController welcomeController;

    @Before
    public void setUp() {
        welcomeController = new WelcomeController();
    }

    @Test
    public void testWelcomePage() {
        ModelAndView mav = welcomeController.welcome();
        Assert.assertEquals("Welcome page view", WELCOME_PAGE, mav.getViewName());
    }
}

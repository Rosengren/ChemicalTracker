package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.User;
import com.chemicaltracker.service.UserService;
import com.chemicaltracker.service.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

public class UserControllerTest {

    private static final String VALID_USER = "valid_user";

    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        userService = mock(UserServiceImpl.class);
        userController = new UserController(userService);
    }

    @Test
    public void testRedirectUserIfAlreadyLoggedIn() {
        Principal principal = () -> VALID_USER;
        ModelAndView mav = userController.login(principal);
        Assert.assertEquals("redirect:/home", mav.getViewName());
    }

    @Test
    public void testDisplayLoginPageIfNotLoggedIn() {
        ModelAndView mav = userController.login(null);
        Assert.assertEquals("login", mav.getViewName());
    }

    @Test
    public void testSignupPageAccess() {
        ModelAndView mav = userController.signUp();
        Assert.assertEquals("signup", mav.getViewName());
    }

    @Test
    public void testValidSignUp() {

        User user = new User();
        when(userService.addUser(user))
                .thenReturn(user);

        ModelAndView mav = userController.signUpPost(user);
        Assert.assertEquals("signup?success=true", mav.getViewName());
    }

    @Test
    public void testSignupError() {

        User user = new User();
        when(userService.addUser(user))
                .thenReturn(null);

        ModelAndView mav = userController.signUpPost(user);
        Assert.assertEquals("signup?success=false", mav.getViewName());
    }
}
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void redirectUserToHomeSinceAlreadyLoggedIn() {
        Principal principal = () -> VALID_USER;
        ModelAndView mav = userController.login(principal);
        Assert.assertEquals("redirect:/home", mav.getViewName());
    }

    @Test
    public void displayLoginPageSinceUserNotLoggedIn() {
        ModelAndView mav = userController.login(null);
        Assert.assertEquals("login", mav.getViewName());
    }

    @Test
    public void visitSignupPage() {
        ModelAndView mav = userController.signUp();
        Assert.assertEquals("signup", mav.getViewName());
    }

    @Test
    public void successfullySignUp() {
        User user = new User();
        when(userService.addUser(user))
                .thenReturn(user);

        ModelAndView mav = userController.signUpPost(user);
        Assert.assertEquals("signup", mav.getViewName());
        Assert.assertTrue((boolean)mav.getModel().get("success"));
    }

    @Test
    public void unsuccessfullySignup() {
        User user = new User();
        when(userService.addUser(user))
                .thenReturn(null);

        ModelAndView mav = userController.signUpPost(user);
        Assert.assertEquals("signup", mav.getViewName());
        Assert.assertFalse((boolean)mav.getModel().get("success"));
    }

    @Test
    public void logoutOfAccount() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        ModelAndView mav = userController.logoutPage(new MockHttpServletRequest(), new MockHttpServletResponse());
        Assert.assertEquals("welcome", mav.getViewName());
    }
}
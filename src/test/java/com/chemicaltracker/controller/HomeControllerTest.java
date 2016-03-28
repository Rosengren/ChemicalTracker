package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.service.InventoryService;
import com.chemicaltracker.service.InventoryServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    private static final String VALID_USER = "user";

    private HomeController homeController;

    @Mock
    private InventoryService inventoryService;

    @Before
    public void setUp() throws Exception {
        inventoryService = mock(InventoryServiceImpl.class);
        homeController = new HomeController(inventoryService);
    }

    @Test
    public void visitHomePage() {

        List<Location> locations = new ArrayList<>();

        when(inventoryService.getLocations(VALID_USER))
                .thenReturn(locations);

        Principal principal = () -> VALID_USER;
        ModelAndView modelAndView = homeController.home(principal);

        assertEquals("home", modelAndView.getViewName());
        assertEquals(locations, modelAndView.getModel().get("locations"));
    }
}

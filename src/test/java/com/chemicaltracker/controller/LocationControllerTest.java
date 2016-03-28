package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.InventoryService;
import com.chemicaltracker.service.InventoryServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationControllerTest {

    private static final String LOCATION_NAME = "location";
    private static final String LOCATION_ID = "id";
    private static final String VALID_USER = "user";

    private LocationController locationController;

    @Mock
    private InventoryService inventoryService;

    @Before
    public void setUp() {
        inventoryService = mock(InventoryServiceImpl.class);
        locationController = new LocationController(inventoryService);
    }

    @Test
    public void visitLocationPage() {

        List<Room> rooms = new ArrayList<>();
        Location location = new Location().withID(LOCATION_ID);

        when(inventoryService.getRooms(VALID_USER, LOCATION_NAME))
                .thenReturn(rooms);

        when(inventoryService.getLocation(VALID_USER, LOCATION_NAME))
                .thenReturn(location);

        Principal principal = () -> VALID_USER;
        ModelAndView modelAndView = locationController.viewLocation(LOCATION_NAME, principal);

        assertEquals("location", modelAndView.getViewName());

        // Make sure these objects are passed to the view
        Map model = modelAndView.getModel();
        assertEquals(LOCATION_ID, model.get("parentID"));
        assertEquals(LOCATION_NAME, model.get("location"));
        assertEquals(rooms, model.get("rooms"));
    }
}
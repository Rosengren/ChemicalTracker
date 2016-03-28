package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Cabinet;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoomControllerTest {

    private static final String VALID_USER = "user";
    private static final String LOCATION_NAME = "location";
    private static final String ROOM_NAME = "room";
    private static final String ROOM_ID = "room_id";

    private RoomController roomController;

    @Mock
    private InventoryService inventoryService;

    @Before
    public void setUp() {
        inventoryService = mock(InventoryServiceImpl.class);
        roomController = new RoomController(inventoryService);
    }

    @Test
    public void visitRoomPage() {

        final List<Cabinet> cabinets = new ArrayList<>();
        final Room room = new Room().withID(ROOM_ID);

        Principal principal = () -> VALID_USER;

        when(inventoryService.getCabinets(VALID_USER, LOCATION_NAME, ROOM_NAME))
                .thenReturn(cabinets);

        when(inventoryService.getRoom(VALID_USER, LOCATION_NAME, ROOM_NAME))
                .thenReturn(room);

        ModelAndView mav = roomController
                .viewRoom(LOCATION_NAME, ROOM_NAME, principal);

        Assert.assertEquals("room", mav.getViewName());
        Map model = mav.getModel();

        Assert.assertEquals(cabinets, model.get("cabinets"));
        Assert.assertEquals(ROOM_ID, model.get("parentID"));
    }
}
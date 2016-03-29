package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.service.InventoryService;
import com.chemicaltracker.service.InventoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChemicalControllerTest {

    private static final String LOCATION_NAME = "location";
    private static final String ROOM_NAME = "room";
    private static final String CABINET_NAME = "cabinet";
    private static final String CHEMICAL_NAME = "chemical";
    private static final String VALID_USER = "user";

    private ChemicalController chemicalController;

    @Mock
    private InventoryService inventoryService;

    @Before
    public void setUp() throws Exception {
        inventoryService = mock(InventoryServiceImpl.class);
        chemicalController = new ChemicalController(inventoryService);
    }

    @Test
    public void visitChemicalPage() {

        Chemical chemical = new Chemical();
        chemical.setName(CHEMICAL_NAME);

        when(inventoryService.getChemical(CHEMICAL_NAME))
                .thenReturn(chemical);

        Principal principal = () -> VALID_USER;
        ModelAndView modelAndView = chemicalController.viewChemical(
                LOCATION_NAME, ROOM_NAME, CABINET_NAME,
                CHEMICAL_NAME, principal);

        assertEquals("chemical", modelAndView.getViewName());

        Map model = modelAndView.getModel();
        assertEquals(chemical, model.get("chemical"));
        assertEquals(LOCATION_NAME, model.get("location"));
        assertEquals(ROOM_NAME, model.get("room"));
        assertEquals(CABINET_NAME, model.get("cabinet"));
        assertEquals(CHEMICAL_NAME, model.get("title"));
    }
}
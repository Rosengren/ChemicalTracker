package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.service.InventoryService;
import com.chemicaltracker.service.InventoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CabinetControllerTest {

    private static final String VALID_USER = "user";
    private static final String LOCATION_NAME = "location";
    private static final String ROOM_NAME = "room";
    private static final String CABINET_NAME = "cabinet";

    private static final String OLD_AUDIT_VERSION = "old";
    private static final String NEW_AUDIT_VERSION = "new";

    private CabinetController cabinetController;
    private Cabinet cabinet;
    private Principal principal;

    @Before
    public void setUp() throws Exception {
        InventoryService inventoryService = mock(InventoryServiceImpl.class);
        cabinetController = new CabinetController(inventoryService);

        createCabinet();

        principal = () -> VALID_USER;

        when(inventoryService.getCabinet(VALID_USER,
                LOCATION_NAME, ROOM_NAME, CABINET_NAME))
                .thenReturn(cabinet);

        when(inventoryService.getChemicalsByName(any()))
                .thenReturn(new ArrayList<>());
    }

    @Test
    public void visitCabinetPageWithoutVersion() {
        ModelAndView modelAndView = cabinetController.viewCabinet(LOCATION_NAME,
                ROOM_NAME, CABINET_NAME, null, principal);

        Map model = modelAndView.getModel();
        assertEquals(NEW_AUDIT_VERSION, model.get("currentAuditVersion"));
    }

    @Test
    public void visitCabinetPageWithVersion() {
        ModelAndView modelAndView = cabinetController.viewCabinet(LOCATION_NAME,
                ROOM_NAME, CABINET_NAME, OLD_AUDIT_VERSION, principal);

        Map model = modelAndView.getModel();
        assertEquals(OLD_AUDIT_VERSION, model.get("currentAuditVersion"));
    }

    @Test
    public void viewCabinetPageElements() {
        ModelAndView modelAndView = cabinetController.viewCabinet(LOCATION_NAME,
                ROOM_NAME, CABINET_NAME, OLD_AUDIT_VERSION, principal);

        Map model = modelAndView.getModel();

        assertEquals(CABINET_NAME, model.get("title"));
        assertEquals(ROOM_NAME, model.get("room"));
        assertEquals(LOCATION_NAME, model.get("location"));

    }

    private void createCabinet() {
        cabinet = new Cabinet().withName(CABINET_NAME);

        Cabinet.AuditVersion oldVersion = new Cabinet.AuditVersion().withName(OLD_AUDIT_VERSION);
        Cabinet.AuditVersion newVersion = new Cabinet.AuditVersion().withName(NEW_AUDIT_VERSION);

        cabinet.addAuditVersion(OLD_AUDIT_VERSION, oldVersion);
        cabinet.addAuditVersion(NEW_AUDIT_VERSION, newVersion);
    }
}


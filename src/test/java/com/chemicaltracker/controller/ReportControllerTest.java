package com.chemicaltracker.controller;

import com.chemicaltracker.service.InventoryService;
import com.chemicaltracker.service.InventoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.*;

import static org.mockito.Mockito.mock;

public class ReportControllerTest {

    private static final String LOCATION_NAME = "location";

    private ReportController reportController;

    @Mock
    private InventoryService inventoryService;

    @Before
    public void setUp() throws Exception {
        inventoryService = mock(InventoryServiceImpl.class);
        reportController = new ReportController(inventoryService);
    }

    @Test
    public void downloadPDF() throws IOException {

        // TODO: unsure what to test
    }
}
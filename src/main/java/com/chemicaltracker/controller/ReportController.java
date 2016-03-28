package com.chemicaltracker.controller;

import java.util.List;
import java.security.Principal;

import com.chemicaltracker.model.ReportDocument;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import com.chemicaltracker.service.InventoryService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Annotations
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/report")
public class ReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private final InventoryService inventoryService;

    @Autowired
    public ReportController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(value = "/generate/{locationName}", method=GET)
    public void downloadPDF(@PathVariable("locationName") String locationName,
                            HttpServletRequest request, HttpServletResponse response,
                            Principal principal) throws IOException {

        final ServletContext servletContext = request.getSession().getServletContext();
        final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        final String temporaryFilePath = tempDirectory.getAbsolutePath();

        final String fileName = locationName + "-report.pdf";
        response.setContentType("application/pdf");

        final String username = principal.getName();
        final Location location = inventoryService.getLocation(username, locationName);
        final List<Room> rooms = inventoryService.getRoomsByIds(username, location.getStoredItemIDs());

        for (Room room : rooms) {

            for (Cabinet cabinet : inventoryService.getCabinetsByIds(username, room.getStoredItemIDs())) {

                inventoryService.getChemicalsByName(cabinet.getStoredItemNames())
                        .forEach(cabinet::addElement);

                room.addElement(cabinet);
            }

            location.addElement(room);
        }

        try {
            ReportDocument.createPDF(temporaryFilePath+"\\"+fileName, ReportDocument.DEFAULT_TITLE, location);
            ByteArrayOutputStream byteArrayOutputStream;
            byteArrayOutputStream = convertPDFToByteArrayOutputStream(temporaryFilePath+"\\"+fileName);
            OutputStream os = response.getOutputStream();
            byteArrayOutputStream.writeTo(os);
            os.flush();
        } catch (Exception e) {
            LOGGER.error("Could not generate PDF", e);
        }
    }

    private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

        InputStream inputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            out = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }

}

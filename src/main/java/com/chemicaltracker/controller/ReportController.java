package com.chemicaltracker.controller;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import com.chemicaltracker.model.*;
import com.chemicaltracker.persistence.*;

import org.springframework.ui.Model;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final LocationDAO locationDB = LocationDAO.getInstance();
    private final CabinetDAO cabinetDB = CabinetDAO.getInstance();
    private final RoomDAO roomDB = RoomDAO.getInstance();

    private final ChemicalDAO chemicalDB =
        ChemicalDAO.getInstance();

    @RequestMapping(value = "/generate/{locationName}", method=GET)
    public void downloadPDF(@PathVariable("locationName") String locationName,
            HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws IOException {

        final ServletContext servletContext = request.getSession().getServletContext();
        final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        final String temperotyFilePath = tempDirectory.getAbsolutePath();

        final String fileName = locationName + "-report.pdf";
        response.setContentType("application/pdf");

        final String username = principal.getName();
        Location location = locationDB.find(username, locationName);

        final List<String> roomIDs = location.getStoredItemIDs();
        final List<Room> rooms = roomDB.findAllRooms(username, roomIDs);

        final Map<Room, List<Cabinet>> roomCabinetMap = new HashMap<Room, List<Cabinet>>();

        // TODO: this logic will be moved to the Models
        for (Room room : rooms) {

            for (Cabinet cabinet : cabinetDB.findAllCabinets(username, room.getStoredItemIDs())) {

                for (Chemical chemical : chemicalDB.batchGetChemicals(cabinet.getStoredItemNames())) {
                    cabinet.addElement(chemical);
                }

                room.addElement(cabinet);
            }

            location.addElement(room);
        }

        try {
            ReportDocument.createPDF(temperotyFilePath+"\\"+fileName, ReportDocument.DEFAULT_TITLE, location);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos = convertPDFToByteArrayOutputStream(temperotyFilePath+"\\"+fileName);
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        return baos;
    }

}

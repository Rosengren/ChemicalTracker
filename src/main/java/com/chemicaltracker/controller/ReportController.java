package com.chemicaltracker.controller;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.ReportDocument;

import com.chemicaltracker.persistence.ChemicalDataAccessObject;
import com.chemicaltracker.persistence.ChemicalDataAccessDynamoDB;

import com.chemicaltracker.persistence.StorageFactory;
import com.chemicaltracker.persistence.StorageDataAccessObject;

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
@RequestMapping("")
public class ReportController {

    private final StorageDataAccessObject locationDB = StorageFactory.getStorage("LOCATIONS");
    private final StorageDataAccessObject roomDB = StorageFactory.getStorage("ROOMS");
    private final StorageDataAccessObject cabinetDB = StorageFactory.getStorage("CABINETS");

    private final ChemicalDataAccessObject chemicalDB = new ChemicalDataAccessDynamoDB();

    @RequestMapping(value = "/downloadPDF", method=GET)
    public void downloadPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // TODO: make this a POST and add: username + locationName to the requestBody;
        final ServletContext servletContext = request.getSession().getServletContext();
        final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        final String temperotyFilePath = tempDirectory.getAbsolutePath();

        String fileName = "report.pdf"; // TODO; use a more meaningfull name
        response.setContentType("application/pdf");

        //String username = principal.getName();
        String username = "kevin"; // FIXME
        String locationName = "Carleton";
        Storage location = locationDB.getStorage(username, locationName);

        final List<String> roomIDs = location.getStoredItemIDs();
        final List<Storage> rooms = roomDB.batchGetStorages(username, roomIDs);

        final Map<Storage, List<Storage>> roomCabinetMap = new HashMap<Storage, List<Storage>>();
        for (Storage room : rooms) {

            List<String> cabinetIDs = room.getStoredItemIDs();
            roomCabinetMap.put(room, cabinetDB.batchGetStorages(username, cabinetIDs));
        }

        try {

            ReportDocument.createPDF(temperotyFilePath+"\\"+fileName, "Chemical Tracker Report", location, roomCabinetMap);
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

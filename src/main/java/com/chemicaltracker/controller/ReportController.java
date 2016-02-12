package com.chemicaltracker.controller;

import java.util.List;
import java.security.Principal;

import com.chemicaltracker.model.ReportDocument;
import com.chemicaltracker.persistence.dao.CabinetDao;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PathVariable;

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

@Controller
@RequestMapping("/report")
public class ReportController {

    private final LocationDao locationsDB = LocationDao.getInstance();
    private final CabinetDao cabinetsDB = CabinetDao.getInstance();
    private final RoomDao roomsDB = RoomDao.getInstance();
    private final ChemicalDao chemicalsDB = ChemicalDao.getInstance();

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
        Location location = locationsDB.find(username, locationName);

        final List<String> roomIDs = location.getStoredItemIDs();
        final List<Room> rooms = roomsDB.findAllByIds(username, roomIDs);

        // TODO: this logic will be moved to the Models
        for (Room room : rooms) {

            for (Cabinet cabinet : cabinetsDB.findAllByIds(username, room.getStoredItemIDs())) {

                for (Chemical chemical : chemicalsDB.findByNames(cabinet.getStoredItemNames())) {
                    cabinet.addElement(chemical);
                }

                room.addElement(cabinet);
            }

            location.addElement(room);
        }

        try {
            ReportDocument.createPDF(temporaryFilePath+"\\"+fileName, ReportDocument.DEFAULT_TITLE, location);
            ByteArrayOutputStream baos;
            baos = convertPDFToByteArrayOutputStream(temporaryFilePath+"\\"+fileName);
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

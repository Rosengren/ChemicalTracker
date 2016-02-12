package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.CabinetDao;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class CabinetController {


    private static final LocationDao locationsDB = LocationDao.getInstance();
    private static final CabinetDao cabinetsDB = CabinetDao.getInstance();
    private static final RoomDao roomsDB = RoomDao.getInstance();
    private static final ChemicalDao chemicalsDB = ChemicalDao.getInstance();

    @RequestMapping("/{locationName}/{roomName}/{cabinetName}")
    public ModelAndView viewCabinet(@PathVariable("locationName") String locationName,
                                  @PathVariable("roomName") String roomName, @PathVariable("cabinetName") String cabinetName,
                                  final Principal principal) {

        final String username = principal.getName();
        final String roomID = locationsDB.find(username, locationName).getStoredItemID(roomName);
        final String cabinetID = roomsDB.find(username, roomID).getStoredItemID(cabinetName);

        final ModelAndView cabinetView = new ModelAndView("cabinet");

        final Cabinet cabinet = cabinetsDB.find(username, cabinetID);
        final List<String> chemicalNames = cabinet.getStoredItemNames();
        final List<Chemical> chemicals = chemicalsDB.findByNames(chemicalNames);

        final Set<String> checklist = new HashSet<>();
        for (Chemical chemical : chemicals) {
            chemical.setImageURL(cabinet.getStoredItemID(chemical.getName()));
            checklist.add(chemical.getHandlingAndStorage().get("Storage"));
        }

        cabinetView.addObject("title", cabinetName);
        cabinetView.addObject("username", username);
        cabinetView.addObject("location", locationName);
        cabinetView.addObject("room", roomName);
        cabinetView.addObject("tags", cabinet.getTags());
        cabinetView.addObject("checklist", checklist);
        cabinetView.addObject("searchChemicalURL", "/api/test/partialQuery/");
        cabinetView.addObject("chemicals", chemicals);
        cabinetView.addObject("addURL", "/api/add/chemicals/to/cabinet/" + cabinetID);
        cabinetView.addObject("removeURL", "/api/remove/chemical/from/cabinet/" + cabinetID);

        return cabinetView;
    }

}

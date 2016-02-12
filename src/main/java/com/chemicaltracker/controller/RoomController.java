package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.dao.CabinetDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Room;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class RoomController {

    private static final LocationDao locationsDB = LocationDao.getInstance();
    private static final CabinetDao cabinetsDB = CabinetDao.getInstance();
    private static final RoomDao roomsDB = RoomDao.getInstance();

    @RequestMapping("/{locationName}/{roomName}")
    public ModelAndView viewRoom(@PathVariable("locationName") final String locationName,
                                     @PathVariable("roomName") final String roomName,
                                     final Principal principal) {

        final String roomID = locationsDB.find(principal.getName(), locationName).getStoredItemID(roomName);
        final Room room = roomsDB.find(principal.getName(), roomID);
        final List<String> cabinetIDs = room.getStoredItemIDs();

        final ModelAndView roomView = new ModelAndView("room");

        roomView.addObject("title", roomName);
        roomView.addObject("username", principal.getName());
        roomView.addObject("cabinets", cabinetsDB.findAllByIds(principal.getName(), cabinetIDs));
        roomView.addObject("parentID", room.getID());
        roomView.addObject("addURL", "/add/cabinet/");
        roomView.addObject("location", locationName);

        return roomView;
    }
}

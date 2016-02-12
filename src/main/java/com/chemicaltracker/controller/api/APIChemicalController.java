package com.chemicaltracker.controller.api;

import com.chemicaltracker.controller.api.request.UpdateRequest;
import com.chemicaltracker.controller.api.response.UpdateResponse;
import com.chemicaltracker.controller.api.response.UpdateStatus;
import com.chemicaltracker.persistence.dao.CabinetDao;
import com.chemicaltracker.persistence.dao.ChemicalDao;
import com.chemicaltracker.persistence.dao.LocationDao;
import com.chemicaltracker.persistence.dao.RoomDao;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.persistence.model.Location;
import com.chemicaltracker.persistence.model.Room;
import java.security.Principal;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api")
public class APIChemicalController {

    private static final ChemicalDao chemicalsDB = ChemicalDao.getInstance();
    private static final LocationDao locationsDB = LocationDao.getInstance();
    private static final CabinetDao cabinetsDB = CabinetDao.getInstance();
    private static final RoomDao roomsDB = RoomDao.getInstance();

    @RequestMapping(value="/update", method=POST)
    public @ResponseBody UpdateResponse update(@RequestBody final UpdateRequest request, final Principal principal) {

        final String requestType = request.getRequestType();

        if (invalidRequest(requestType)) {
            return new UpdateResponse(UpdateStatus.INVALID_REQUEST_TYPE);
        }

        if (missingStorageField(request)) {
            return new UpdateResponse(UpdateStatus.MISSING_STORAGE_FIELD);
        }

        final Chemical chemical = chemicalsDB.find(request.getChemical());

        if (chemical == null) {
            return new UpdateResponse(UpdateStatus.INVALID_CHEMICAL);
        }

        final Cabinet cabinet = getCabinet(principal.getName(), request);

        if (requestType.equals("ADD")) {
            cabinet.addChemical(request.getChemical(), "#"); // TODO: add image URL
            cabinetsDB.update(cabinet);
            return new UpdateResponse(UpdateStatus.ADDED_CHEMICAL);
        } else if (requestType.equals("REMOVE")) {
            cabinet.removeChemical(request.getChemical());
            cabinetsDB.update(cabinet);
            return new UpdateResponse(UpdateStatus.REMOVED_CHEMICAL);
        }

        return new UpdateResponse(UpdateStatus.UNKNOWN_ERROR);
    }

    private boolean invalidRequest(final String request) {
        return !request.equals("ADD") && !request.equals("REMOVE");
    }

    private boolean missingStorageField(final UpdateRequest request) {
        return request.getLocation().isEmpty() ||
               request.getRoom().isEmpty()     ||
               request.getCabinet().isEmpty();
    }

    private Cabinet getCabinet(final String username, final UpdateRequest request) {
        final Location location = locationsDB.find(username, request.getLocation());
        final Room room = roomsDB.find(username, location.getStoredItemID(request.getRoom()));
        return cabinetsDB.find(username, room.getStoredItemID(request.getCabinet()));
    }
}

package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.service.InventoryService;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/Home", "/home"})
public class CabinetController {

    private final InventoryService inventoryService;

    @Autowired
    public CabinetController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/{locationName}/{roomName}/{cabinetName}")
    public ModelAndView viewCabinet(@PathVariable("locationName") String locationName,
                                  @PathVariable("roomName") String roomName, @PathVariable("cabinetName") String cabinetName,
                                  final Principal principal) {

        final String username = principal.getName();
        final ModelAndView cabinetView = new ModelAndView("cabinet");

        final List<Chemical> chemicals = inventoryService.getChemicals(username, locationName, roomName, cabinetName);
        final Cabinet cabinet = inventoryService.getCabinet(username, locationName, roomName, cabinetName);

        cabinetView.addObject("title", cabinetName);
        cabinetView.addObject("username", username);
        cabinetView.addObject("location", locationName);
        cabinetView.addObject("room", roomName);
        cabinetView.addObject("tags", cabinet.getTags());
        cabinetView.addObject("checklist", getChecklist(chemicals));
        cabinetView.addObject("searchChemicalURL", "/api/test/partialQuery/");
        cabinetView.addObject("chemicals", chemicals);
        cabinetView.addObject("addURL", "/api/add/chemicals/to/cabinet/" + cabinet.getID());
        cabinetView.addObject("removeURL", "/api/remove/chemical/from/cabinet/" + cabinet.getID());

        return cabinetView;
    }


    private Set<String> getChecklist(final List<Chemical> chemicals) {
        return chemicals.stream()
                .map(chemical -> chemical.getHandlingAndStorage().get("Storage"))
                .collect(Collectors.toSet());
    }

}

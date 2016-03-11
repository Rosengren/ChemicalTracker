package com.chemicaltracker.controller;

import com.chemicaltracker.controller.api.response.CompareCabinetsResponse;
import com.chemicaltracker.model.Comparison;
import com.chemicaltracker.persistence.model.Cabinet;
import com.chemicaltracker.persistence.model.Chemical;
import com.chemicaltracker.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
                                    @PathVariable("roomName") String roomName,
                                    @PathVariable("cabinetName") String cabinetName,
                                    @RequestParam(required = false, name = "v") final String version,
                                    final Principal principal) {

        final String username = principal.getName();
        final ModelAndView cabinetView = new ModelAndView("cabinet");

        final Cabinet cabinet = inventoryService.getCabinet(username, locationName, roomName, cabinetName);
        List<Chemical> chemicals;

        Cabinet.AuditVersion auditVersion;
        if (version != null && cabinet.getAuditVersion(version) != null) {
            auditVersion = cabinet.getAuditVersion(version);
        } else {
            auditVersion = cabinet.getLatestAuditVersion();
        }

        cabinetView.addObject("tags", auditVersion.getTags());
        cabinetView.addObject("metrics", auditVersion.getMetrics());
        cabinetView.addObject("currentAuditVersion", auditVersion.getName());
        chemicals = inventoryService.getChemicalsByName(auditVersion.getChemicalNames());
        final Map<String, String> chemicalMap = auditVersion.getChemicals();

        cabinetView.addObject("title", cabinetName);
        cabinetView.addObject("username", username);
        cabinetView.addObject("location", locationName);
        cabinetView.addObject("room", roomName);
        cabinetView.addObject("checklist", getChecklist(chemicals));
        cabinetView.addObject("searchChemicalURL", "/api/test/partialQuery/");
        cabinetView.addObject("chemicals", chemicalMap);
        cabinetView.addObject("cabinetID", cabinet.getID());
        cabinetView.addObject("addURL", "/api/add/chemicals/to/cabinet/" + cabinet.getID());
        cabinetView.addObject("removeURL", "/api/remove/chemical/from/cabinet/" + cabinet.getID());
        cabinetView.addObject("updateImageURL", "/api/update/chemicalImage");

        cabinetView.addObject("auditVersionNames", cabinet.getAuditVersionNames());


        return cabinetView;
    }


    @RequestMapping(value = "/{locationName}/{roomName}/{cabinetName}/compare/{old}/with/{new}", method = GET)
    public ResponseEntity<CompareCabinetsResponse> compareVersions(@PathVariable("locationName") String locationName,
                                                                   @PathVariable("roomName") String roomName,
                                                                   @PathVariable("cabinetName") String cabinetName,
                                                                   @PathVariable("old") final String oldVersion,
                                                                   @PathVariable("new") final String newVersion,
                                                                   final Principal principal) {

        final Cabinet cabinet = inventoryService.getCabinet(principal.getName(), locationName, roomName, cabinetName);
        final Comparison comparison = inventoryService.compareCabinetVersions(cabinet, oldVersion, newVersion);

        final CompareCabinetsResponse response = new CompareCabinetsResponse();
        response.setAdded(comparison.getAddedChemicals());
        response.setRemoved(comparison.getRemovedChemicals());
        response.setMatching(comparison.getMatchingChemicals());
        response.setOldVersion(oldVersion);
        response.setNewVersion(newVersion);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Set<String> getChecklist(final List<Chemical> chemicals) {
        return chemicals.stream()
                .map(chemical -> chemical.getHandlingAndStorage().get("Storage"))
                .collect(Collectors.toSet());
    }

}

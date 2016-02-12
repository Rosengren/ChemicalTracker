package com.chemicaltracker.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

// Annotations
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/api")
public class AuthorizeController {

    @RequestMapping(value="/authorize", method=POST)
    public @ResponseBody ResponseEntity verifyAuthenticationCredentials() {
        return new ResponseEntity(HttpStatus.OK);
    }
}

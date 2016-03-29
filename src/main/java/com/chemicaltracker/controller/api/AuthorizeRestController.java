package com.chemicaltracker.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/api")
public class AuthorizeRestController {

    @RequestMapping(value="/authorize")
    public @ResponseBody ResponseEntity verifyAuthenticationCredentials(final Principal principal) {
        return new ResponseEntity(HttpStatus.OK);
    }
}
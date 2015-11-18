package com.chemicaltracker.controller;

import org.springframework.ui.Model;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    private final UserService userService = new UserService();
    private final UserCreateFormValidator userCreateFormValidator =
        new UserCreateFormValidator();

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/user/create", method = GET)
    public ModelAndView getUserCreatePage(Model model) {
        return new ModelAndView("user_create", "form", new UserCreateForm());
    }

    @RequestMapping(value = "/user/create", method = POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_create";
        }

        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }

        return "user_create"; // TODO: Change this
    }
}

package com.mvc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mvc.NotFoundException;
import com.mvc.ParseException;

@ControllerAdvice
class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exc, Model model) {
        model.addAttribute("message", exc.getMessage());
        return "/exception";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ParseException.class)
    public String handleParseException(NotFoundException exc, Model model) {
        model.addAttribute("message", exc.getMessage());
        return "/exception";
    }
}

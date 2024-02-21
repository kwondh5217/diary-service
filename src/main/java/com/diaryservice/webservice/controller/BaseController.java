package com.diaryservice.webservice.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class BaseController {
    @ExceptionHandler(IllegalAccessException.class)
    public String exceptionHandler(IllegalArgumentException e, HttpSession session){
        session.setAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }
}

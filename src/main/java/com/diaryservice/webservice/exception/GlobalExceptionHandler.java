package com.diaryservice.webservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.IllformedLocaleException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("데이터 제약 조건 위반");
        modelAndView.setViewName("error");

        return modelAndView;
    }

    @ExceptionHandler(IllformedLocaleException.class)
    public ModelAndView handleNotValidAccess(IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("잘못된 접근입니다.");
        modelAndView.setViewName("error");

        return modelAndView;
    }


}
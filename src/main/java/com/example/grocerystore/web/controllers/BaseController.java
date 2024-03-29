package com.example.grocerystore.web.controllers;

import org.springframework.web.servlet.ModelAndView;

import static com.example.grocerystore.util.constants.AppConstants.*;


public abstract class BaseController {
    protected ModelAndView view(String view, ModelAndView modelAndView) {
        modelAndView.setViewName(view);

        return modelAndView;
    }

    protected ModelAndView view(String view) {
        return this.view(view, new ModelAndView());
    }

    protected ModelAndView redirect(String url){
        return this.view(REDIRECT_BASE_CONTROLLER + url);
    }
}

package com.example.grocerystore.web.controllers;

import org.modelmapper.ModelMapper;

import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.domain.models.view.CategoryViewModel;
import com.example.grocerystore.service.CategoryService;

import com.example.grocerystore.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.grocerystore.util.constants.AppConstants.*;

@Controller
public class HomeController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService, ModelMapper modelMapper) {

        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PageTitle(INDEX)
    public ModelAndView renderIndexPage(Principal principal, ModelAndView modelAndView) {

        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);

        return view("/index", modelAndView);
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle(HOME)
    public ModelAndView renderHomePage(Principal principal, ModelAndView modelAndView) {
        
        List<CategoryViewModel> categories =
                mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories());
        
        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);

        return view("/home", modelAndView);
    }

    private List<CategoryViewModel> mapCategoryServiceToViewModel(List<CategoryServiceModel> categoryServiceModels){
        return categoryServiceModels.stream()
                .map(product -> modelMapper.map(product, CategoryViewModel.class))
                .collect(Collectors.toList());
    }
}

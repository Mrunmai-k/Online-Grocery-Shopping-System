package com.example.grocerystore.web.controllers;

import org.modelmapper.ModelMapper;
import com.example.grocerystore.domain.models.binding.ProductAddBindingModel;
import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.domain.models.service.ProductServiceModel;
import com.example.grocerystore.domain.models.view.CategoryViewModel;
import com.example.grocerystore.domain.models.view.ProductAllViewModel;
import com.example.grocerystore.domain.models.view.ProductDetailsViewModel;
import com.example.grocerystore.util.error.ProductNameAlreadyExistsException;
import com.example.grocerystore.util.error.ProductNotFoundException;
import com.example.grocerystore.service.*;
import com.example.grocerystore.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.grocerystore.util.constants.AppConstants.*;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, UserService userService,
                             CloudinaryService cloudinaryService, CategoryService categoryService,
                             ModelMapper modelMapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    @PageTitle("Add Products")
    public ModelAndView addProduct(@ModelAttribute(name = MODEL) ProductAddBindingModel model,
                                   ModelAndView modelAndView) {
        return loadAndReturnModelAndView(model, modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    public ModelAndView addProductConfirm(@Valid @ModelAttribute(name = MODEL) ProductAddBindingModel model,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView) throws IOException {
        ProductServiceModel productServiceModel = modelMapper.map(model, ProductServiceModel.class);

        if (bindingResult.hasErrors() || model.getImage().isEmpty() ||
                productService.createProduct(productServiceModel, model.getImage()) == null) {

            return loadAndReturnModelAndView(model, modelAndView);
        }
        return redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    @PageTitle("Products")
    public ModelAndView allProducts(ModelAndView modelAndView) {

        List<ProductAllViewModel> productAllViewModels =
                mapProductServiceToViewModel(productService.findAllFilteredProducts());

        modelAndView.addObject(PRODUCTS_TO_LOWERCASE, productAllViewModels);

        return view("product/all-products", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Product Details")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductDetailsViewModel product =
                modelMapper.map(productService.findProductById(id), ProductDetailsViewModel.class);
        modelAndView.addObject(PRODUCT_TO_LOWERCASE, product);

        return view("product/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Product")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView,
                                    @ModelAttribute(MODEL) ProductAddBindingModel productAddBindingModel) {

        productAddBindingModel =
                this.modelMapper.map(productService.findProductById(id), ProductAddBindingModel.class);

        List<CategoryViewModel> categoryViewModelList =
                mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories());

        modelAndView.addObject(MODEL, productAddBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categoryViewModelList);

        modelAndView.addObject(PRODUCT_ID, id);

        return view("product/edit-product", modelAndView);
    }


    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    public ModelAndView editProductConfirm(ModelAndView modelAndView, @PathVariable String id,
                                           @Valid @ModelAttribute(MODEL) ProductAddBindingModel model,
                                           BindingResult bindingResult) throws IOException {

        boolean isNewImageUploaded = !model.getImage().isEmpty();

        initImage(model);

        ProductServiceModel productServiceModel = modelMapper.map(model, ProductServiceModel.class);

        productService.editProduct(id, productServiceModel, isNewImageUploaded, model.getImage());

        if (bindingResult.hasErrors() ||
                productService.editProduct(id, productServiceModel, isNewImageUploaded, model.getImage())==null) {
            modelAndView.addObject(CATEGORIES_TO_LOWER_CASE,
                    mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories()));
            modelAndView.addObject(MODEL, model);
            modelAndView.addObject(PRODUCT_ID, id);
            return this.view("product/edit-product", modelAndView);
        }

        return redirect("/products/details/" + id);
    }

    private void initImage(ProductAddBindingModel model) {
        if (model.getImage().isEmpty()){
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return ZERO_NUMBER;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return new byte[ZERO_NUMBER];
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return null;
                }

                @Override
                public void transferTo(File file) throws IOException, IllegalStateException {

                }
            };
            model.setImage(multipartFile);
        }
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Product")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView,
                                      @ModelAttribute("model") ProductAddBindingModel productAddBindingModel) {

        productAddBindingModel = this.modelMapper.map(productService.findProductById(id), ProductAddBindingModel.class);

        modelAndView.addObject(MODEL, productAddBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE,
                mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories()));

        modelAndView.addObject(PRODUCT_ID, id);

        return view("product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {

        productService.deleteProduct(id);

        return redirect("/products/all");
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView fetchByCategory(@PathVariable String category, ModelAndView modelAndView) {

        List<ProductAllViewModel> products = new ArrayList<>();

        products = category.equals("All")? mapProductServiceToViewModel(productService.findAllFilteredProducts())
                : mapProductServiceToViewModel(productService.findAllByCategoryFilteredProducts(category));

        modelAndView.addObject(CATEGORY_NAME, category);

        modelAndView.addObject(PRODUCTS_TO_LOWERCASE, products);

        return view("product/show-products", modelAndView);
    }

    @GetMapping("/fetch")
    @ResponseBody
    public List<ProductAllViewModel> fetchAllProducts() {

        return mapProductServiceToViewModel(productService.findAllFilteredProducts());

    }

    @GetMapping("/api/find")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public List<ProductAllViewModel> searchProducts(@RequestParam(PRODUCT_TO_LOWERCASE) String product) {

        return mapProductServiceToViewModel(productService.findProductsByPartOfName(product));
    }

    private ModelAndView loadAndReturnModelAndView(ProductAddBindingModel productBindingModel,
                                                   ModelAndView modelAndView) {

        List<CategoryViewModel> categories = mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories());

        modelAndView.addObject(MODEL, productBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);

        return view("product/add-product", modelAndView);
    }

    private List<ProductAllViewModel> mapProductServiceToViewModel(List<ProductServiceModel> productServiceModels){
        return productServiceModels.stream()
                .map(product -> modelMapper.map(product, ProductAllViewModel.class))
                .collect(Collectors.toList());
    }

    private List<CategoryViewModel> mapCategoryServiceToViewModel(List<CategoryServiceModel> categoryServiceModels){
        return categoryServiceModels.stream()
                .map(product -> modelMapper.map(product, CategoryViewModel.class))
                .collect(Collectors.toList());
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ModelAndView handleProductNotFound(ProductNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView(ERROR);
        modelAndView.addObject(MESSAGE, e.getMessage());
        modelAndView.addObject(STATUS_CODE, e.getStatusCode());

        return modelAndView;
    }

    @ExceptionHandler({ProductNameAlreadyExistsException.class})
    public ModelAndView handleProductNameALreadyExist(ProductNameAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView(ERROR);
        modelAndView.addObject(MESSAGE, e.getMessage());
        modelAndView.addObject(STATUS_CODE, e.getStatusCode());

        return modelAndView;
    }
}
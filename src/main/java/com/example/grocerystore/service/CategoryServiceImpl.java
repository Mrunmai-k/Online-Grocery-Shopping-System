package com.example.grocerystore.service;

import org.modelmapper.ModelMapper;
import com.example.grocerystore.domain.entities.Category;
import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.util.error.CategoryNameAlreadyExistsException;
import com.example.grocerystore.util.error.CategoryNotFoundException;
import com.example.grocerystore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

import static com.example.grocerystore.util.constants.ExceptionMessages.CATEGORY_NAME_EXIST_EX_MSG;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {

        if (!validator.validate(categoryServiceModel).isEmpty()) {
            throw new CategoryNotFoundException();
        }
        if (categoryRepository.findByName(categoryServiceModel.getName())
                .orElse(null) != null) {
            throw new CategoryNameAlreadyExistsException(CATEGORY_NAME_EXIST_EX_MSG);
        }
        
        Category category = this.modelMapper.map(categoryServiceModel, Category.class);

        return this.modelMapper.map(this.categoryRepository.saveAndFlush(category),
                CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .filter(c->!c.isDeleted())
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel findCategoryById(String id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (category.isDeleted()){
            throw new CategoryNotFoundException();
        }

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.setName(categoryServiceModel.getName());

        return this.modelMapper.map(this.categoryRepository.saveAndFlush(category),
                CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel deleteCategory(String id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        /*category.setDeleted(true);
        this.categoryRepository.save(category);*/
        this.categoryRepository.deleteById(id);
        

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> findAllFilteredCategories() {
        return findAllCategories().stream()
                .filter(c->!c.isDeleted())
                .collect(Collectors.toList());
    }
}

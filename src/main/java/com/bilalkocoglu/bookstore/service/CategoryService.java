package com.bilalkocoglu.bookstore.service;

import com.bilalkocoglu.bookstore.exception.ModelNotFoundException;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(int id){
        Optional<Category> categoryOtp = categoryRepository.findById(id);
        if (categoryOtp.isPresent()){
            log.info(categoryOtp.get().getId() + " - Category Called !");
            return categoryOtp.get();
        }
        else{
            log.error(id + " - Category Not Found !");
            throw new ModelNotFoundException(categoryNotFoundMessage);
        }
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category createCategory(String name){
        Category category = new Category();
        category.setName(name);

        return categoryRepository.save(category);
    }

    @Value("${exception.category.notfound}")
    private String categoryNotFoundMessage;
}

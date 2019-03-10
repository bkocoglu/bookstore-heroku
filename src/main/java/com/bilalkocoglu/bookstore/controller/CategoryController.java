package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.BookCreateRequest;
import com.bilalkocoglu.bookstore.dto.CategoryCreateRequest;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = CategoryController.END_POINT)
public class CategoryController {
    public static final String END_POINT = "/category";
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{categoryId}")
    public ResponseEntity getCategoryById(@PathVariable("categoryId") int categoryId){
        Category category = categoryService.getCategoryById(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @GetMapping()
    public ResponseEntity getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping()
    public ResponseEntity createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest){
        Category category = categoryService.createCategory(categoryCreateRequest.getName());

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}

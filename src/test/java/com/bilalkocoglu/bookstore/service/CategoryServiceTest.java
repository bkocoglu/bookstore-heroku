package com.bilalkocoglu.bookstore.service;

import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import com.bilalkocoglu.bookstore.exception.ModelNotFoundException;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.repository.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {
    CategoryRepository categoryRepository;
    CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void getCategoryById() {
        Category category = new Category(1, "test");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());


        Assert.assertEquals(category, categoryService.getCategoryById(1));

        Exception ex = null;
        try {
            categoryService.getCategoryById(2);
        }catch (ModelNotFoundException exception){
            ex = exception;
        }
        Assert.assertNotNull(ex);
    }


    @Test
    public void getAllCategories() {
        List<Category> categories = Stream.of(
                new Category(1,"test"),
                new Category(2,"test1"),
                new Category(3, "test2")
                ).collect(Collectors.toList());

        when(categoryRepository.findAll()).thenReturn(categories);

        Assert.assertEquals(categories, categoryService.getAllCategories());

    }

    @Test
    public void createCategory() {
        Category category = new Category(1, "test");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Assert.assertEquals(category, categoryService.createCategory(anyString()));
    }
}

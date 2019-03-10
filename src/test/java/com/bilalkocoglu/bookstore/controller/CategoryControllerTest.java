package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.CategoryCreateRequest;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryControllerTest {

    private MockMvc mvc;
    private ObjectWriter objectWriter;

    private CategoryService categoryService;
    private CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer();

        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);
        mvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void getCategoryById() throws Exception {
        Category category = new Category(1, "testCategory");

        when(categoryService.getCategoryById(category.getId())).thenReturn(category);

        String uri = "/category/" + category.getId();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(category)));
    }

    @Test
    public void getAllCategories() throws Exception {
        List<Category> categories = Stream.of(
                new Category(1,"testCategory1"),
                new Category(2,"testCategory2"),
                new Category(3, "testCategory3")
        ).collect(Collectors.toList());

        when(categoryService.getAllCategories()).thenReturn(categories);

        String uri = "/category";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(categories)));
    }

    @Test
    public void createCategory() throws Exception {
        Category category = new Category(1, "testCategory");


        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(category.getName());

        when(categoryService.createCategory(categoryCreateRequest.getName())).thenReturn(category);

        String uri = "/category";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectWriter.writeValueAsBytes(categoryCreateRequest))
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(category)));
    }
}
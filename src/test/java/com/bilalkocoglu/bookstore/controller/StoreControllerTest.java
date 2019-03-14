package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.AddBookRequest;
import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import com.bilalkocoglu.bookstore.dto.StoreCreateRequest;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Ownership;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.service.StoreService;
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
public class StoreControllerTest {

    private MockMvc mvc;
    private ObjectWriter objectWriter;


    private StoreService storeService;
    private StoreController storeController;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer();

        storeService = mock(StoreService.class);
        storeController = new StoreController(storeService);
        mvc = MockMvcBuilders.standaloneSetup(storeController).build();
    }

    @Test
    public void getStoreById() throws Exception {
        Store store = new Store(1, "testStore");

        when(storeService.getStoreById(store.getId())).thenReturn(store);

        String uri = "/store/" + store.getId();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(store)));
    }

    @Test
    public void getAllStores() throws Exception {
        List<Store> stores = Stream.of(
                new Store(1,"testStore1"),
                new Store(2,"testStore2"),
                new Store(3, "testStore3")
        ).collect(Collectors.toList());

        when(storeService.getAllStores()).thenReturn(stores);

        String uri = "/store";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(stores)));
    }

    @Test
    public void createStore() throws Exception {
        Store store = new Store(1, "testStore");


        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(store.getName());

        when(storeService.createStore(storeCreateRequest.getName())).thenReturn(store);

        String uri = "/store";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectWriter.writeValueAsBytes(storeCreateRequest))
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(store)));
    }

    @Test
    public void addBook() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1,"testBook",category);
        Store store = new Store(1,"testStore");
        Ownership ownership = new Ownership(1, book, store, 100);

        AddBookRequest addBookRequest = new AddBookRequest(store.getId(), book.getId(), ownership.getPrice());
        AddBookRequest addBookRequest1 = new AddBookRequest(1, 1, 1);

        when(storeService.addBook(addBookRequest.getStoreId(), addBookRequest.getBookId(), addBookRequest.getPrice())).thenReturn(ownership);
        when(storeService.addBook(addBookRequest1.getStoreId(), addBookRequest1.getBookId(), addBookRequest1.getPrice())).thenReturn(null);

        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .message("Kitap Zaten Bu Kitapcida Mevcut !")
                .build();

        String uri = "/store/book";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectWriter.writeValueAsBytes(addBookRequest))
        ).andDo(print());

        ResultActions resultActions1 = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectWriter.writeValueAsBytes(addBookRequest1))
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(ownership)));

        resultActions1
                .andExpect(status().is(400))
                .andExpect(content().json(objectWriter.writeValueAsString(exceptionResponse)));

    }

    @Test
    public void removeBook() throws Exception {

        String uri = "/store/" + anyInt() + "/book/" + anyInt();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andDo(print());

        resultActions.andExpect(status().isOk());
    }
}
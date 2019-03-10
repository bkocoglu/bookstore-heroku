package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.BookByStoreResponse;
import com.bilalkocoglu.bookstore.dto.BookCreateRequest;
import com.bilalkocoglu.bookstore.dto.BookPattern;
import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.service.BookService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookControllerTest {

    private MockMvc mvc;

    private BookService bookService;
    private StoreService storeService;

    private BookController bookController;

    private ObjectWriter objectWriter;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer();

        bookService = mock(BookService.class);
        storeService = mock(StoreService.class);
        bookController = new BookController(bookService,storeService);
        mvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getBookById() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testBook", category);

        when(bookService.getBookById(1)).thenReturn(book);

        String uri = "/book/" + book.getId();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(book)));
    }

    @Test
    public void getAllBooks() throws Exception {

        Category category = new Category(1, "testCategory");

        List<Book> books = Stream.of(
                new Book(1,"test", category),
                new Book(2,"test1", category),
                new Book(3, "test2", category)
        ).collect(Collectors.toList());

        when(bookService.getAllBooks()).thenReturn(books);

        String uri = "/book";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(books)));

    }

    @Test
    public void createBook() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testBook", category);


        BookCreateRequest bookCreateRequest = new BookCreateRequest("deneme", 1);

        when(bookService.createBook(bookCreateRequest.getName(), bookCreateRequest.getCategoryId())).thenReturn(book);

        String uri = "/book";

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectWriter.writeValueAsBytes(bookCreateRequest))
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(book)));
    }

    @Test
    public void getBooksByCategory() throws Exception {
        Category category = new Category(1, "testCategory");

        List<Book> books = Stream.of(
                new Book(1,"test", category),
                new Book(2,"test1", category),
                new Book(3, "test2", category)
        ).collect(Collectors.toList());

        when(bookService.getBooksByCategory(category.getId())).thenReturn(books);

        String uri = "/book/category/"+ category.getId();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(books)));

    }

    @Test
    public void getBooksByStore() throws Exception {
        Category category = new Category(1, "testCategory");
        Store store = new Store(1,"testStore");
        Store store1 = new Store(2,"testStore");

        Book book = new Book(1,"testBook",category);


        BookByStoreResponse bookByStoreResponse = BookByStoreResponse
                .builder()
                .books(Stream.of(
                        new BookPattern(book, 50),
                        new BookPattern(book, 50),
                        new BookPattern(book, 50),
                        new BookPattern(book, 50)
                ).collect(Collectors.toList()))
                .build();

        BookByStoreResponse bookByStoreResponse1 = BookByStoreResponse
                .builder()
                .books(new ArrayList<>())
                .build();

        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .message("Aradiginiz Kitapcida Kitap Bulunamadi !")
                .build();

        when(storeService.getStoreById(store.getId())).thenReturn(store);
        when(storeService.getStoreById(store1.getId())).thenReturn(store1);
        when(bookService.getBookByStore(store)).thenReturn(bookByStoreResponse);
        when(bookService.getBookByStore(store1)).thenReturn(bookByStoreResponse1);

        String uri = "/book/store/"+ store.getId();
        String uri1 = "/book/store/"+ store1.getId();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        ResultActions resultActions1 = mvc.perform(
                MockMvcRequestBuilders.get(uri1).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(bookByStoreResponse)));

        resultActions1
                .andExpect(status().is(404))
                .andExpect(content().json(objectWriter.writeValueAsString(exceptionResponse)));

    }

    @Test
    public void updateCategory() throws Exception {
        Category category = new Category(1, "testCategory");
        Category category1 = new Category(2, "testCategory1");

        Book book = new Book(1,"testBook",category);
        Book book1 = new Book(1,"testBook",category1);

        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .message("Kitabın kategorisi zaten bu !")
                .build();


        when(bookService.updateCategory(category1.getId(), book.getId())).thenReturn(book1);
        when(bookService.updateCategory(category.getId(), book.getId())).thenReturn(null);


        String uri = "/book/" + book.getId() + "/category/" + category1.getId();
        String uri1 = "/book/" + book.getId() + "/category/" + category.getId();


        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.put(uri).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        ResultActions resultActions1 = mvc.perform(
                MockMvcRequestBuilders.put(uri1).accept(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(book1)));

        resultActions1
                .andExpect(status().is(404))
                .andExpect(content().json(objectWriter.writeValueAsString(exceptionResponse)));
    }
}
package com.bilalkocoglu.bookstore.service;

import com.bilalkocoglu.bookstore.dto.BookByStoreResponse;
import com.bilalkocoglu.bookstore.dto.BookPattern;
import com.bilalkocoglu.bookstore.exception.ModelNotFoundException;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Ownership;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.repository.BookRepository;
import com.bilalkocoglu.bookstore.repository.OwnershipRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    private BookRepository bookRepository;
    private CategoryService categoryService;
    private OwnershipRepository ownershipRepository;

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        categoryService = mock(CategoryService.class);
        ownershipRepository = mock(OwnershipRepository.class);
        bookRepository = mock(BookRepository.class);

        bookService = new BookService(bookRepository, categoryService, ownershipRepository);
    }

    @Test
    public void getBookById() {
        Category category = new Category(1, "testCategory");

        Book book = new Book(1, "testBook", category);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.findById(2)).thenReturn(Optional.empty());


        Assert.assertEquals(book, bookService.getBookById(1));

        Exception ex = null;
        try {
            bookService.getBookById(2);
        }catch (ModelNotFoundException exception){
            ex = exception;
        }
        Assert.assertNotNull(ex);
    }

    @Test
    public void getAllBooks() {
        Category category = new Category(1, "testCategory");

        List<Book> books = Stream.of(
                new Book(1,"test", category),
                new Book(2,"test1", category),
                new Book(3, "test2", category)
        ).collect(Collectors.toList());

        when(bookRepository.findAll()).thenReturn(books);

        Assert.assertEquals(books, bookService.getAllBooks());
    }

    @Test
    public void createBook() {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "test", category);

        when(categoryService.getCategoryById(1)).thenReturn(category);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Assert.assertEquals(book, bookService.createBook(anyString(), 1));
    }

    @Test
    public void getBooksByCategory() {
        Category category = new Category(1, "testCategory");

        List<Book> books = Stream.of(
                new Book(1,"test", category),
                new Book(2,"test1", category),
                new Book(3, "test2", category)
        ).collect(Collectors.toList());

        when(categoryService.getCategoryById(1)).thenReturn(category);
        when(bookRepository.findByCategory(category)).thenReturn(books);

        Assert.assertEquals(books, bookService.getBooksByCategory(1));
    }

    @Test
    public void getBookByStore() {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1,"testBook",category);
        Store store = new Store(1, "testStore");

        List<Ownership> ownerships = Stream.of(
                new Ownership(1,book, store, 50),
                new Ownership(1,book, store, 50),
                new Ownership(1,book, store, 50),
                new Ownership(1,book, store, 50)
        ).collect(Collectors.toList());

        when(ownershipRepository.findByStore(store)).thenReturn(ownerships);

        BookByStoreResponse bookByStoreResponse = BookByStoreResponse
                .builder()
                .books(Stream.of(
                        new BookPattern(book, 50),
                        new BookPattern(book, 50),
                        new BookPattern(book, 50),
                        new BookPattern(book, 50)
                        ).collect(Collectors.toList()))
                .build();

        Assert.assertEquals(bookByStoreResponse, bookService.getBookByStore(store));
    }

}

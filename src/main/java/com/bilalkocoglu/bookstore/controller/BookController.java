package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.BookByStoreResponse;
import com.bilalkocoglu.bookstore.dto.BookCreateRequest;
import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.service.BookService;
import com.bilalkocoglu.bookstore.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = BookController.END_POINT)
public class BookController {
    public static final String END_POINT = "/book";
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;
    private StoreService storeService;

    public BookController(BookService bookService,
                          StoreService storeService){
        this.bookService = bookService;
        this.storeService = storeService;
    }

    @GetMapping(value = "/{bookId}")
    public ResponseEntity getBookById(@PathVariable("bookId") int bookId){
        Book book = bookService.getBookById(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping()
    public ResponseEntity getAllBooks(){
        List<Book> books = bookService.getAllBooks();

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @PostMapping()
    public ResponseEntity createBook(@Valid @RequestBody BookCreateRequest bookCreateRequest){

        Book book = bookService.createBook(bookCreateRequest.getName(), bookCreateRequest.getCategoryId());

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity getBooksByCategory(@PathVariable("categoryId") int categoryId){
        List<Book> books = bookService.getBooksByCategory(categoryId);

        if (books.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            ExceptionResponse
                                    .builder()
                                    .message("Aradiginiz Kategoride Kitap Bulunamadi !")
                                    .build()
                    );
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(books);
        }
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity getBooksByStore(@PathVariable("storeId") int storeId){
        Store store = storeService.getStoreById(storeId);
        BookByStoreResponse res= bookService.getBookByStore(store);

        if (res.getBooks().isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            ExceptionResponse
                                    .builder()
                                    .message("Aradiginiz Kitapcida Kitap Bulunamadi !")
                                    .build()
                    );
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }

    @PutMapping("/{bookId}/category/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable("bookId") int bookId,
                                         @PathVariable("categoryId") int categoryId){
        Book book = bookService.updateCategory(categoryId, bookId);

        if (book == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            ExceptionResponse
                                    .builder()
                                    .message("Kitabın kategorisi zaten bu !")
                                    .build()
                    );
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
    }
}

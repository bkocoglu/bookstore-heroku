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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource(value = "classpath:message.properties")
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;
    private CategoryService categoryService;
    private OwnershipRepository ownershipRepository;

    public BookService(BookRepository bookRepository,
                       CategoryService categoryService,
                       OwnershipRepository ownershipRepository){
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.ownershipRepository = ownershipRepository;
    }

    public Book getBookById(int id){
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()){
            log.info(bookOpt.get().getId() + " - Book Called !");
            return bookOpt.get();
        }
        else{
            log.error(id + " - Book Not Found !");
            throw new ModelNotFoundException(bookNotFoundMessage);
        }
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book createBook(String name, int categoryId){
        Category category = categoryService.getCategoryById(categoryId);

        Book book = new Book();
        book.setName(name);
        book.setCategory(category);

        book = bookRepository.save(book);

        log.info(book.getId() + " - Book Save !");
        return book;
    }

    public List<Book> getBooksByCategory(int categoryId){
        Category category = categoryService.getCategoryById(categoryId);

        return bookRepository.findByCategory(category);
    }

    public BookByStoreResponse getBookByStore(Store store){
        List<Ownership> ownerships = ownershipRepository.findByStore(store);
        List<BookPattern> books = new ArrayList<>();

        for (Ownership ownership: ownerships) {
            books.add(BookPattern
                    .builder()
                    .book(ownership.getBook())
                    .price(ownership.getPrice())
                    .build()
            );
        }
        return BookByStoreResponse.builder().books(books).build();
    }

    public Book updateCategory(int categoryId, int bookId){
        Category category = categoryService.getCategoryById(categoryId);
        Book book = getBookById(bookId);

        if (book.getCategory() == category){
            log.info("Category is already equal ! Dont need Change");
            return null;
        } else {
            book.setCategory(category);
            book = bookRepository.save(book);
            return book;
        }
    }

    @Value("${exception.book.notfound}")
    private String bookNotFoundMessage;

    @Value("${exception.category.notfound}")
    private String categoryNotFoundMessage;
}

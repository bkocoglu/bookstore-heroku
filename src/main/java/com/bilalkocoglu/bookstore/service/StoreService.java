package com.bilalkocoglu.bookstore.service;

import com.bilalkocoglu.bookstore.exception.ModelNotFoundException;
import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Ownership;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.repository.OwnershipRepository;
import com.bilalkocoglu.bookstore.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private static final Logger log = LoggerFactory.getLogger(StoreService.class);

    private StoreRepository storeRepository;
    private BookService bookService;
    private OwnershipRepository ownershipRepository;

    public StoreService(StoreRepository storeRepository,
                        BookService bookService,
                        OwnershipRepository ownershipRepository){
        this.storeRepository = storeRepository;
        this.bookService = bookService;
        this.ownershipRepository = ownershipRepository;
    }

    public Store getStoreById(int id){
        Optional<Store> storeOpt = storeRepository.findById(id);
        if (storeOpt.isPresent()){
            log.info(storeOpt.get().getId() + " - Store Called !");
            return storeOpt.get();
        }
        else{
            log.error(id + " - Store Not Found !");
            throw new ModelNotFoundException(storeNotFoundMessage);
        }
    }

    public List<Store> getAllStores(){
        return storeRepository.findAll();
    }

    public Store createStore(String name){
        Store store = new Store();
        store.setName(name);

        return storeRepository.save(store);
    }

    private boolean isAlreadyTakenBook(Store store, Book book){
        List<Ownership> ownerships = ownershipRepository.findByStoreAndBook(store, book);
        if(ownerships.isEmpty())
            return false;
        else
            return true;
    }

    public Ownership addBook(int storeId, int bookId, float price){
        Store store = getStoreById(storeId);
        Book book = bookService.getBookById(bookId);

        if(!isAlreadyTakenBook(store, book)){
            Ownership ownership = new Ownership();
            ownership.setBook(book);
            ownership.setStore(store);
            ownership.setPrice(price);

            ownership = ownershipRepository.save(ownership);
            log.info(ownership.getId() + " - Saved !");
            return ownership;
        }else{
            log.info("Already Token Book !");
            return null;
        }
    }

    public void removeBook(int storeId, int bookId){
        Store store = getStoreById(storeId);
        Book book = bookService.getBookById(bookId);

        if (isAlreadyTakenBook(store, book)){
            ownershipRepository.removeByStoreAndBook(store, book);
            log.info(book.getName() + " is remove in " + store.getName());
        }else {
            log.info("Book Not Already Available !");
            throw new ModelNotFoundException(ownershipNotFoundMessage);
        }
    }

    @Value("${exception.store.notfound}")
    private String storeNotFoundMessage;

    @Value("${exception.ownership.notfound}")
    private String ownershipNotFoundMessage;
}

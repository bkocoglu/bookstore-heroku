package com.bilalkocoglu.bookstore.controller;

import com.bilalkocoglu.bookstore.dto.AddBookRequest;
import com.bilalkocoglu.bookstore.dto.CategoryCreateRequest;
import com.bilalkocoglu.bookstore.dto.ExceptionResponse;
import com.bilalkocoglu.bookstore.dto.StoreCreateRequest;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Ownership;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = StoreController.END_POINT)
public class StoreController {
    public static final String END_POINT = "/store";
    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    private StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @GetMapping(value = "/{storeId}")
    public ResponseEntity getStoreById(@PathVariable("storeId") int storeId){
        Store store = storeService.getStoreById(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @GetMapping()
    public ResponseEntity getAllStores(){
        List<Store> stores = storeService.getAllStores();

        return ResponseEntity.status(HttpStatus.OK).body(stores);
    }

    @PostMapping()
    public ResponseEntity createStore(@Valid @RequestBody StoreCreateRequest storeCreateRequest){
        Store store = storeService.createStore(storeCreateRequest.getName());

        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @PostMapping(value = "/book")
    public ResponseEntity addBook(@Valid @RequestBody AddBookRequest addBookRequest){


        Ownership ownership = storeService.addBook(addBookRequest.getStoreId(), addBookRequest.getBookId(), addBookRequest.getPrice());

        if (ownership != null){
            return ResponseEntity.status(HttpStatus.OK).body(ownership);
        }else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            ExceptionResponse
                                    .builder()
                                    .message("Kitap Zaten Bu Kitapcida Mevcut !")
                                    .build()
                    );
        }
    }

    @DeleteMapping(value = "/{storeId}/book/{bookId}")
    public ResponseEntity removeBook(@PathVariable("storeId") int storeId,
                                     @PathVariable("bookId") int bookId){
        storeService.removeBook(storeId, bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.bilalkocoglu.bookstore.service;

import com.bilalkocoglu.bookstore.exception.ModelNotFoundException;
import com.bilalkocoglu.bookstore.model.Category;
import com.bilalkocoglu.bookstore.model.Store;
import com.bilalkocoglu.bookstore.repository.OwnershipRepository;
import com.bilalkocoglu.bookstore.repository.StoreRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoreServiceTest {

    private StoreRepository storeRepository;
    private BookService bookService;
    private OwnershipRepository ownershipRepository;
    private StoreService storeService;

    @Before
    public void setUp() throws Exception {
        storeRepository = mock(StoreRepository.class);
        bookService = mock(BookService.class);
        ownershipRepository = mock(OwnershipRepository.class);

        storeService = new StoreService(storeRepository, bookService, ownershipRepository);
    }

    @Test
    public void getStoreById() {
        Store store = new Store(1, "testStore");


        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(storeRepository.findById(2)).thenReturn(Optional.empty());


        Assert.assertEquals(store, storeService.getStoreById(1));

        Exception ex = null;
        try {
            storeService.getStoreById(2);
        }catch (ModelNotFoundException exception){
            ex = exception;
        }
        Assert.assertNotNull(ex);
    }

    @Test
    public void getAllStores() {
        List<Store> stores = Stream.of(
                new Store(1,"testStore"),
                new Store(2,"testStore1"),
                new Store(3, "testStore2")
        ).collect(Collectors.toList());

        when(storeRepository.findAll()).thenReturn(stores);

        Assert.assertEquals(stores, storeService.getAllStores());
    }

    @Test
    public void createStore() {
        Store store = new Store(1, "testStore");

        when(storeRepository.save(any(Store.class))).thenReturn(store);

        Assert.assertEquals(store, storeService.createStore(anyString()));
    }
}

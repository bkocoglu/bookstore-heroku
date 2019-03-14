package com.bilalkocoglu.bookstore.repository;

import com.bilalkocoglu.bookstore.model.Book;
import com.bilalkocoglu.bookstore.model.Ownership;
import com.bilalkocoglu.bookstore.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OwnershipRepository extends JpaRepository<Ownership, Integer> {
    List<Ownership> findByStoreAndBook(Store store, Book book);

    List<Ownership> findByStore(Store store);

    @Transactional
    void removeByStoreAndBook(Store store, Book book);
}

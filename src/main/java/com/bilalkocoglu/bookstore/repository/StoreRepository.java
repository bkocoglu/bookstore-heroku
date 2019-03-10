package com.bilalkocoglu.bookstore.repository;

import com.bilalkocoglu.bookstore.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
}

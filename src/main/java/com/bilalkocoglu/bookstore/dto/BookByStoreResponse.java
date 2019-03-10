package com.bilalkocoglu.bookstore.dto;

import com.bilalkocoglu.bookstore.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BookByStoreResponse {
    private List<BookPattern> books;
}



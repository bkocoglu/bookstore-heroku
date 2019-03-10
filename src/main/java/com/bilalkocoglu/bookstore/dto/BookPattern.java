package com.bilalkocoglu.bookstore.dto;

import com.bilalkocoglu.bookstore.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookPattern{
    private Book book;
    private float price;
}
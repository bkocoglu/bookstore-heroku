package com.bilalkocoglu.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
public class AddBookRequest {
    @Positive
    @NotNull
    private int storeId;

    @Positive
    @NotNull
    private int bookId;

    @NotNull
    @Positive
    private float price;
}

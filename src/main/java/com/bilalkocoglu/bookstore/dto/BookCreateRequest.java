package com.bilalkocoglu.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
public class BookCreateRequest {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Positive
    private int categoryId;
}

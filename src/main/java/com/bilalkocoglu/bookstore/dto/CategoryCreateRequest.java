package com.bilalkocoglu.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class CategoryCreateRequest {
    @NotEmpty
    @NotNull
    private String name;
}

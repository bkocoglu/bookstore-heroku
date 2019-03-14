package com.bilalkocoglu.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class StoreCreateRequest {
    @NotNull
    @NotEmpty
    private String name;
}

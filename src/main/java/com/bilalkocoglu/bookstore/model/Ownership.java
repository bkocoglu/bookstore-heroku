package com.bilalkocoglu.bookstore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Ownership")
public class Ownership {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Store store;

    @Positive
    @Column(nullable = false)
    private float price;
}

package com.e_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Description is Required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greter than 0")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock must be 0 or more")
    @NotNull(message = "Stock is required")
    private int stock;

    @NotBlank(message = "Category is Required")
    private String category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

}

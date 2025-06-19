package com.e_commerce.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;
    private Long sellerId;

}


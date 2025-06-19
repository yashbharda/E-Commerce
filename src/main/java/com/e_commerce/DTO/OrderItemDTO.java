package com.e_commerce.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Long productId;

    private int quantity;

    @JsonIgnore
    private BigDecimal price;
}

package com.e_commerce.DTO;

import com.e_commerce.Entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;         // Only show user ID
    private String userName;     // Only show user name
    private LocalDateTime orderDate;
    private String status;
    private Double total;
    private List<OrderItemDTO> items;
}

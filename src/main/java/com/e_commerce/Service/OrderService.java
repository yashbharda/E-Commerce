package com.e_commerce.Service;

import com.e_commerce.DTO.OrderDTO;
import com.e_commerce.Entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order saveOrder(OrderDTO orderDTO); // Insert order with items and user

    List<OrderDTO> getAllOrders(); // Get all orders

    Optional<OrderDTO> getOrderById(Long id); // Get order by ID

    boolean deleteOrderById(Long id); // Delete order

    boolean updateOrderStatus(Long id, Order order); // Optional: update only status

    List<OrderDTO> getOrdersByUserId(Long userId);

}

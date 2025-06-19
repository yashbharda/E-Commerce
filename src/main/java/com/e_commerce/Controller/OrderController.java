package com.e_commerce.Controller;

import com.e_commerce.DTO.OrderDTO;
import com.e_commerce.DTO.OrderItemDTO;
import com.e_commerce.Entity.Order;
import com.e_commerce.Entity.OrderItem;
import com.e_commerce.Entity.User;
import com.e_commerce.Service.OrderService;
import com.e_commerce.Service.OrderServiceImpl;
import com.e_commerce.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Order Apis", description = "Place, Read, Update, Delete Order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping("/order/add")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("You must log in first to place an order.");
        }

        // Set user ID from session
        orderDTO.setUserId(user.getId());

        // Save order
        Order savedOrder = orderService.saveOrder(orderDTO);

        OrderDTO responseDTO = mapper.map(savedOrder, OrderDTO.class);

        // Map items
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        if (savedOrder.getItems() != null) {
            for (OrderItem item : savedOrder.getItems()) {
                OrderItemDTO dto = new OrderItemDTO();
                dto.setProductId(item.getProduct().getId());
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPrice());
                itemDTOs.add(dto);
            }
        }
        responseDTO.setItems(itemDTOs);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestParam Long userId) {
        Optional<User> userOpt=userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }
        User user=userOpt.get();
        if(!user.isLoggedIn()){
            return ResponseEntity.status(403).body("Please login first");
        }
        if(!"Admin".equalsIgnoreCase(user.getRole())){
            return ResponseEntity.status(403).body("Only admin can access all orders");
        }
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<OrderDTO> orderDTO = orderService.getOrderById(id);

        User loggedUser=new User();
        if (!loggedUser.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please login first");
        }


        if (orderDTO.isPresent()) {
            return ResponseEntity.ok(orderDTO.get());
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/order/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order, HttpSession session){
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please Login First");
        }

        // ✅ Only ADMIN allowed
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only admin can update orders");
        }

        boolean updated = orderService.updateOrderStatus(id, order);
        if (updated) {
            return ResponseEntity.ok("Order updated successfully");
        } else {
            return ResponseEntity.status(404).body("Order not found");
        }
    }


    // Only User can see own Order
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please Login First");
        }

        List<OrderDTO> userOrders = orderService.getOrdersByUserId(user.getId());
        return ResponseEntity.ok(userOrders);
    }


// Delete A Order
    @DeleteMapping("/order/del/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id, HttpSession session){
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please Login First");
        }

        Optional<OrderDTO> optionalOrder=orderService.getOrderById(id);
        if(optionalOrder.isEmpty()){
            return ResponseEntity.status(404).body("Order not found");
        }

        OrderDTO orderDTO = optionalOrder.get();
        if("Admin".equalsIgnoreCase(user.getRole()) ||
            orderDTO.getUserId().equals(user.getId())){
            orderService.deleteOrderById(id);
            return ResponseEntity.ok("Order deleted successfully");
        }

        return ResponseEntity.status(403).body("You are not allowed to delete this order");

    }
}

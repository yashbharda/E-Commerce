package com.e_commerce.Service;

import com.e_commerce.DTO.OrderDTO;
import com.e_commerce.DTO.OrderItemDTO;
import com.e_commerce.Entity.Order;
import com.e_commerce.Entity.OrderItem;
import com.e_commerce.Entity.Product;
import com.e_commerce.Entity.User;
import com.e_commerce.Repository.OrderRepository;
import com.e_commerce.Repository.ProductRepository;
import com.e_commerce.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ModelMapper mapper;



    // Order Place

    @Override
    @Transactional
    public Order saveOrder(OrderDTO orderDTO) {

        Order order=new Order();
        order.setStatus(orderDTO.getStatus());
        order.setOrderDate(LocalDateTime.now());

        // Set User
        User user=userRepo.findById(orderDTO.getUserId()).orElseThrow(()->
                new RuntimeException("User not found"));
        order.setUser(user);


        // Order Item and Total
        List<OrderItem> items=new ArrayList<>();
        double total=0;

        for (OrderItemDTO dto: orderDTO.getItems()){
            Product product=productRepo.findById(dto.getProductId()).orElseThrow(()->
                    new RuntimeException("Product Not Found"));

            int orderQuantity=dto.getQuantity();
            int currentStock=product.getStock();

            // // ❌ Check stock availability
            if (orderQuantity > currentStock) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough stock for product:"+product.getName());
            }

            product.setStock(currentStock - orderQuantity);
            productRepo.save(product);


            // Create order item
            OrderItem item=new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(product.getPrice());

            total += product.getPrice().doubleValue() * dto.getQuantity();
            items.add(item);
        }
        order.setItems(items);
        order.setTotal(total);

        return orderRepo.save(order);
    }


    // All Order Get
    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());

            if (order.getUser() != null) {
                orderDTO.setUserId(order.getUser().getId());
            }

            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setTotal(order.getTotal());

            List<OrderItemDTO> itemDTOS = new ArrayList<>();
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    if (item.getProduct() != null) {
                        itemDTO.setProductId(item.getProduct().getId());
                    }
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTOS.add(itemDTO);
                }
            }

            orderDTO.setItems(itemDTOS);
            orderDTOS.add(orderDTO);
        }

        return orderDTOS;
    }

    @Override
    public Optional<OrderDTO> getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDTO orderDTO = new OrderDTO();

            orderDTO.setId(order.getId());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setTotal(order.getTotal());

            if (order.getUser() != null) {
                orderDTO.setUserId(order.getUser().getId());
            }

            List<OrderItemDTO> itemDTOS = new ArrayList<>();
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    if (item.getProduct() != null) {
                        itemDTO.setProductId(item.getProduct().getId());
                    }
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTOS.add(itemDTO);
                }
            }

            orderDTO.setItems(itemDTOS);
            return Optional.of(orderDTO);
        }
        return Optional.empty();
    }


    @Override
    public boolean deleteOrderById(Long id) {
        if(orderRepo.existsById(id)){
            orderRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateOrderStatus(Long id, Order order) {
        Optional<Order> optionalOrder=orderRepo.findById(id);
        if (optionalOrder.isPresent()){
            Order existOrder=optionalOrder.get();
            existOrder.setStatus(order.getStatus());
            orderRepo.save(existOrder);
            return true;
        }
        return false;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders=orderRepo.findOrdersByUserId(userId);
        List<OrderDTO> orderDTOList=new ArrayList<>();

        for (Order order:orders){
            OrderDTO orderDTO=mapper.map(order,OrderDTO.class);
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }
}

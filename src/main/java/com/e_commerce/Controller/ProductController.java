package com.e_commerce.Controller;

import com.e_commerce.DTO.ProductDTO;
import com.e_commerce.Entity.Product;
import com.e_commerce.Entity.User;
import com.e_commerce.Service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Product Apis", description = "Add, Read, Update, Delete Product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    // ✅ Only ADMIN can add product
    @PostMapping("/product/add")
    @Operation(summary = "Admin can add a new Product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO, HttpSession session)
    {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please login first");
        }
        if (!"SELLER".equalsIgnoreCase(user.getRole()) &&!"ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only Admin or Seller can add products");
        }

        return new ResponseEntity<>(productService.saveProduct(productDTO,user), HttpStatus.CREATED);
    }

    // ✅ Anyone (User/Admin) can view all products
    @GetMapping("/product/get")
    @Operation(summary = "Admin and User Get All Product")
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    // ✅ Only ADMIN can update product
    @PutMapping("/product/update/{id}")
    @Operation(summary = "Admin can update product")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Product product, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        Optional<Product> optionalProduct=productService.getProductById(id);
        if(optionalProduct.isEmpty()){
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }

        //Check Permisson
        if("SELLER".equalsIgnoreCase(user.getRole())) {
            if(product.getSeller()==null || !product.getSeller().getId().equals(user.getId())){
                return ResponseEntity.status(403).body("You can only delete your own products");
            }
        }

        // Admin can every product update
        if (productService.updateProductById(id, product)) {
            return new ResponseEntity<>("Product Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
    }

    // ✅ Only ADMIN can delete product
    @DeleteMapping("/product/delete/{id}")
    @Operation(summary = "ADMIN can delete product")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        Product product = optionalProduct.get();

        // SELLER can delete only their own product
        if("SELLER".equalsIgnoreCase(user.getRole())) {
            if(product.getSeller()==null || !product.getSeller().getId().equals(user.getId())){
                return ResponseEntity.status(403).body("You can only delete your own products");
            }
        }
        // ADMIN can delete any product
        if (productService.deleteById(id)) {
            return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
    }

    // ✅ Anyone (User/Admin) can view product by ID
    @GetMapping("/product/get/{id}")
    @Operation(summary = "ADMIN and User can view product by Id")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

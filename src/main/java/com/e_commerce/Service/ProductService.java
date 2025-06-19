package com.e_commerce.Service;

import com.e_commerce.DTO.ProductDTO;
import com.e_commerce.Entity.Product;
import com.e_commerce.Entity.User;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public ProductDTO saveProduct(ProductDTO product, User seller);

    // READ ALL
    public List<ProductDTO> getAllProducts();

    // READ ONE
    public Optional<Product> getProductById(Long id);

    // UPDATE
    public Boolean updateProductById(Long id, Product product);

    // DELETE
    public Boolean deleteById(Long id);
}

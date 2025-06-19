package com.e_commerce.Repository;

import com.e_commerce.Entity.Product;
import com.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

package com.e_commerce.Service;

import com.e_commerce.DTO.ProductDTO;
import com.e_commerce.Entity.Product;
import com.e_commerce.Entity.User;
import com.e_commerce.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO, User seller) {
        Product product=mapper.map(productDTO,Product.class);
        product.setSeller(seller);
        Product saved=productRepo.save(product);
        return mapper.map(saved,ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products=productRepo.findAll();
        List<ProductDTO> productDTOList=new ArrayList<>();
        for (Product product:products){
            ProductDTO productDTO=mapper.map(product,ProductDTO.class);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    @Override
    public Boolean updateProductById(Long id, Product product) {
       if(productRepo.existsById(id)){
           product.setId(id);
           productRepo.save(product);
           return true;
       }
       return false;
    }

    @Override
    public Boolean deleteById(Long id) {
        if(productRepo.existsById(id)){
            productRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
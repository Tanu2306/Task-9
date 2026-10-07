package com.ecommerce.service.impl;

import com.ecommerce.model.entity.Product;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsForTenant(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByTenantId(tenantId);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}

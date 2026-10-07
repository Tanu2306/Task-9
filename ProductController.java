package com.ecommerce.controller;

import com.ecommerce.model.entity.Product;
import com.ecommerce.security.TenantContext;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        String tenantId = TenantContext.getTenantId();
        List<Product> products = productService.getProductsForTenant(tenantId);
        return ResponseEntity.ok(products);
    }
}

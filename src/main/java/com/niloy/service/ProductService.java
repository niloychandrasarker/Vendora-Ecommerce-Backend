package com.niloy.service;

import com.niloy.exceptions.ProductExeption;
import com.niloy.modal.Product;
import com.niloy.modal.Seller;
import com.niloy.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);

    public void deleteProduct(Long productId) throws ProductExeption;

    public Product updateProduct(Long productId, Product product) throws ProductExeption;

    Product findProductById(Long productId) throws ProductExeption;

    List<Product> searchProducts(String query);

    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );
    List<Product> getProductsBySellerId(Long sellerId);
}

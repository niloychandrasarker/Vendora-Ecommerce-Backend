package com.niloy.service.Impl;

import com.niloy.exceptions.ProductExeption;
import com.niloy.modal.Category;
import com.niloy.modal.Product;
import com.niloy.modal.Seller;
import com.niloy.repository.CategoryRepository;
import com.niloy.repository.ProductRepository;
import com.niloy.request.CreateProductRequest;
import com.niloy.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        // Level 1 Category
        Category level1Category = categoryRepository.findByCategoryId(req.getCategory());
        if (level1Category == null) {
            level1Category = new Category();
            level1Category.setCategoryId(req.getCategory());
            level1Category.setLevel(1);
            level1Category = categoryRepository.save(level1Category);
        }

        // Level 2 Category
        Category level2Category = categoryRepository.findByCategoryId(req.getCategory2());
        if (level2Category == null) {
            level2Category = new Category();
            level2Category.setCategoryId(req.getCategory2());
            level2Category.setLevel(2);
            level2Category.setParentCategory(level1Category);
            level2Category = categoryRepository.save(level2Category);
        }

        // Level 3 Category
        Category level3Category = categoryRepository.findByCategoryId(req.getCategory3());
        if (level3Category == null) {
            level3Category = new Category();
            level3Category.setCategoryId(req.getCategory3());
            level3Category.setLevel(3);
            level3Category.setParentCategory(level2Category);
            level3Category = categoryRepository.save(level3Category);
        }

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(level3Category);
        product.setTitle(req.getTitle());
        product.setDescription(req.getDescription());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setImages(req.getImages());
        product.setCreatedAt(LocalDateTime.now());
        product.setDiscountPercentage(discountPercentage);

        return productRepository.save(product);
    }


    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0) {
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductExeption {
        Product product = findProductById(productId);
        productRepository.delete(product);

    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductExeption {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductExeption {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductExeption("Product not found with id: " + productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors != null && !colors.isEmpty()) {

                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }


            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }

            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0,10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}

package com.niloy.repository;

import com.niloy.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long Id);

    //    @Query("SELECT p FROM Product p WHERE " +
//            "(:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
//            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%')))")

    @Query("SELECT p FROM Product p WHERE (:query IS NULL OR LOWER(p.title) " +
            "LIKE LOWER(CONCAT('%', :query, '%') ) ) " +
            "OR (:query IS NULL OR LOWER(p.category.name) " +
            "LIKE LOWER(CONCAT('%', :query, '%') ) )")
    List<Product> searchProduct(@Param("query") String query);

}
    
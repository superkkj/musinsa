package com.dev.musinsa.database.repository;

import com.dev.musinsa.controller.response.BrandPriceSummary;
import com.dev.musinsa.database.entity.Product;
import com.dev.musinsa.database.type.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE (p.category, p.price) IN " +
            "(SELECT p2.category, MIN(p2.price) FROM Product p2 GROUP BY p2.category)")
    List<Product> findCheapestProductsByCategory();


    @Query("SELECT new com.dev.musinsa.controller.response.BrandPriceSummary(p.brand, SUM(p.price)) " +
            "FROM Product p " +
            "GROUP BY p.brand " +
            "HAVING COUNT(DISTINCT p.category) = (SELECT COUNT(DISTINCT category) FROM Product) " +
            "ORDER BY SUM(p.price) ASC " +
            "LIMIT 1")
    Optional<BrandPriceSummary> findLowestPriceBrandByAllCategories();

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> findProductsByBrand(@Param("brand") String brand);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price IN ((SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = :category), (SELECT MAX(p2.price) FROM Product p2 WHERE p2.category = :category))")
    List<Product> findMinMaxPriceProductsByCategory(@Param("category") Category category);

}

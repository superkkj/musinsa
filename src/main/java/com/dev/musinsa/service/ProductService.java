package com.dev.musinsa.service;

import com.dev.musinsa.controller.response.*;
import com.dev.musinsa.database.entity.Product;
import com.dev.musinsa.database.repository.ProductRepository;
import com.dev.musinsa.database.type.Category;
import com.dev.musinsa.domain.ProductCollection;
import com.dev.musinsa.exception.ErrorCode;
import com.dev.musinsa.exception.advice.CommonException;
import com.dev.musinsa.service.cmd.ProductAddCommand;
import com.dev.musinsa.service.cmd.ProductUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional(readOnly = true)
    public List<ProductsResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        return ProductsResponse.from(products);
    }

    @Transactional(readOnly = true)
    public CheapestProductResponse getCheapestProductsByCategory() {
        List<Product> products = productRepository.findCheapestProductsByCategory();

        ProductCollection productCollection = new ProductCollection(products);

        List<Product> cheaperProducts = productCollection.getCheapestProductsByCategory();
        String totalPrice = productCollection.calculateTotalPrice(cheaperProducts);

        return CheapestProductResponse.from(cheaperProducts, totalPrice);
    }

    @Transactional(readOnly = true)
    public LowestPriceBrandResponse findLowestPriceBrand() {
        BrandPriceSummary summary = productRepository.findLowestPriceBrandByAllCategories()
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_PRODUCT));

        List<Product> products = productRepository.findProductsByBrand(summary.brand());
        ProductCollection productCollection = new ProductCollection(products);

        List<CategoryPrice> categoryPrices = productCollection.getCategoryPrices();

        return LowestPriceBrandResponse.from(summary.brand(), categoryPrices, summary.totalPrice());
    }


    @Transactional(readOnly = true)
    public CategoryPriceRangeResponse getCategoryPriceRange(String categoryName) {
        Category category = Category.fromDisplayName(categoryName);

        List<Product> products = productRepository.findMinMaxPriceProductsByCategory(category);
        ProductCollection productCollection = new ProductCollection(products);

        return CategoryPriceRangeResponse.from(
                categoryName,
                productCollection.getCheapestProduct(),
                productCollection.getMostExpensiveProduct()
        );
    }

    @Transactional
    public Long addProduct(ProductAddCommand cmd) {
        Product product = Product.from(cmd);
        return productRepository.save(product).getId();
    }


    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public void update(Long id, ProductUpdateCommand cmd) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_PRODUCT));
        product.update(cmd);
        productRepository.saveAndFlush(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new CommonException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        productRepository.deleteById(id);
    }


}
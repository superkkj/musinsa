package com.dev.musinsa.service;

import com.dev.musinsa.controller.response.BrandPrice;
import com.dev.musinsa.controller.response.CategoryPriceRangeResponse;
import com.dev.musinsa.controller.response.CheapestProductResponse;
import com.dev.musinsa.controller.response.LowestPriceBrandResponse;
import com.dev.musinsa.database.entity.Product;
import com.dev.musinsa.database.repository.ProductRepository;
import com.dev.musinsa.database.type.Category;
import com.dev.musinsa.exception.advice.CommonException;
import com.dev.musinsa.service.cmd.ProductAddCommand;
import com.dev.musinsa.service.cmd.ProductUpdateCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
@DisplayName("ProductService 테스트")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 테스트")
    void getCheapestProductsByCategoryTest() {
        // Given
        productRepository.saveAll(List.of(
                Product.builder().category(Category.TOP).brand("BrandA").price(10000).build(),
                Product.builder().category(Category.TOP).brand("BrandB").price(9000).build(),
                Product.builder().category(Category.OUTER).brand("BrandA").price(20000).build(),
                Product.builder().category(Category.OUTER).brand("BrandB").price(21000).build(),
                Product.builder().category(Category.PANTS).brand("BrandA").price(15000).build(),
                Product.builder().category(Category.PANTS).brand("BrandB").price(14000).build()
        ));

        CheapestProductResponse response = productService.getCheapestProductsByCategory();

        assertNotNull(response);
        assertEquals(3, response.products().size());

        // 각 카테고리별 최저가 상품 확인
        assertTrue(response.products().stream().anyMatch(p -> p.category().equals("상의") && p.brand().equals("BrandB") && p.price().equals("9,000")));
        assertTrue(response.products().stream().anyMatch(p -> p.category().equals("아우터") && p.brand().equals("BrandA") && p.price().equals("20,000")));
        assertTrue(response.products().stream().anyMatch(p -> p.category().equals("바지") && p.brand().equals("BrandB") && p.price().equals("14,000")));

        // 총 가격 확인
        assertEquals("43,000", response.totalPrice());
    }

    @Test
    @DisplayName("최저가 브랜드 조회 테스트")
    void findLowestPriceBrandTest() {
        // Given
        productRepository.saveAll(List.of(
                Product.builder().category(Category.TOP).brand("BrandA").price(10000).build(),
                Product.builder().category(Category.OUTER).brand("BrandA").price(20000).build(),
                Product.builder().category(Category.PANTS).brand("BrandA").price(15000).build(),
                Product.builder().category(Category.TOP).brand("BrandB").price(9000).build(),
                Product.builder().category(Category.OUTER).brand("BrandB").price(21000).build(),
                Product.builder().category(Category.PANTS).brand("BrandB").price(14000).build()
        ));

        // When
        LowestPriceBrandResponse response = productService.findLowestPriceBrand();

        // Then
        assertNotNull(response);
        assertEquals("BrandB", response.brand());
        assertEquals(3, response.categories().size());
        assertEquals("44,000", response.totalPrice());

        // 각 카테고리별 가격 확인
        assertTrue(response.categories().stream().anyMatch(cp -> cp.category().equals("상의") && cp.price().equals("9,000")));
        assertTrue(response.categories().stream().anyMatch(cp -> cp.category().equals("아우터") && cp.price().equals("21,000")));
        assertTrue(response.categories().stream().anyMatch(cp -> cp.category().equals("바지") && cp.price().equals("14,000")));
    }

    @Test
    @DisplayName("카테고리별 최저가와 최고가 상품 조회 테스트")
    void getCategoryPriceRangeTest() {
        // Given
        productRepository.saveAll(List.of(
                Product.builder().category(Category.TOP).brand("BrandA").price(10000).build(),
                Product.builder().category(Category.TOP).brand("BrandB").price(15000).build(),
                Product.builder().category(Category.TOP).brand("BrandC").price(20000).build(),
                Product.builder().category(Category.OUTER).brand("BrandA").price(30000).build(),
                Product.builder().category(Category.OUTER).brand("BrandB").price(35000).build()
        ));

        // When
        CategoryPriceRangeResponse topResponse = productService.getCategoryPriceRange("상의");
        CategoryPriceRangeResponse outerResponse = productService.getCategoryPriceRange("아우터");

        // Then
        // 상의 카테고리 검증
        assertNotNull(topResponse);
        assertEquals("상의", topResponse.category());
        assertEquals(1, topResponse.lowestPrices().size());
        assertEquals(1, topResponse.highestPrices().size());

        BrandPrice topLowest = topResponse.lowestPrices().get(0);
        assertEquals("BrandA", topLowest.brand());
        assertEquals("10,000", topLowest.price());

        BrandPrice topHighest = topResponse.highestPrices().get(0);
        assertEquals("BrandC", topHighest.brand());
        assertEquals("20,000", topHighest.price());

        // 아우터 카테고리 검증
        assertNotNull(outerResponse);
        assertEquals("아우터", outerResponse.category());
        assertEquals(1, outerResponse.lowestPrices().size());
        assertEquals(1, outerResponse.highestPrices().size());

        BrandPrice outerLowest = outerResponse.lowestPrices().get(0);
        assertEquals("BrandA", outerLowest.brand());
        assertEquals("30,000", outerLowest.price());

        BrandPrice outerHighest = outerResponse.highestPrices().get(0);
        assertEquals("BrandB", outerHighest.brand());
        assertEquals("35,000", outerHighest.price());
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void addProductTest() {
        // Given
        ProductAddCommand command = new ProductAddCommand(Category.TOP, "TestBrand", 10000);

        // When
        Long productId = productService.addProduct(command);

        // Then
        assertNotNull(productId);
        Product savedProduct = productRepository.findById(productId).orElse(null);
        assertNotNull(savedProduct);
        assertEquals(Category.TOP, savedProduct.getCategory());
        assertEquals("TestBrand", savedProduct.getBrand());
        assertEquals(10000, savedProduct.getPrice());
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void updateProductTest() {
        // Given
        Product product = productRepository.save(Product.builder()
                .category(Category.TOP)
                .brand("OldBrand")
                .price(10000)
                .build());
        ProductUpdateCommand command = new ProductUpdateCommand(Category.OUTER, "NewBrand", 20000);

        // When
        productService.update(product.getId(), command);

        // Then
        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(Category.OUTER, updatedProduct.getCategory());
        assertEquals("NewBrand", updatedProduct.getBrand());
        assertEquals(20000, updatedProduct.getPrice());
    }

    @Test
    @DisplayName("존재하지 않는 상품 수정 시 예외 발생 테스트")
    void updateNonExistentProductTest() {
        // Given
        ProductUpdateCommand command = new ProductUpdateCommand(Category.TOP, "Brand", 10000);

        // When & Then
        assertThrows(CommonException.class, () -> productService.update(999L, command));
    }

    @Test
    @DisplayName("동시에 여러 요청이 같은 상품을 수정할 때 낙관적 락 테스트")
    void concurrentProductUpdateTest() throws InterruptedException {
        // Given
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Product product = transactionTemplate.execute(status -> productRepository.save(Product.builder()
                .category(Category.TOP)
                .brand("OriginalBrand")
                .price(10000)
                .build()));

        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadNumber = i;
            executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await(); // 모든 스레드가 준비될 때까지 대기
                    transactionTemplate.execute(status -> {
                        try {
                            ProductUpdateCommand command = new ProductUpdateCommand(
                                    Category.OUTER,
                                    "NewBrand" + threadNumber,
                                    20000 + threadNumber
                            );
                            assert product != null;
                            productService.update(product.getId(), command);
                            successCount.incrementAndGet();
                        } catch (ObjectOptimisticLockingFailureException e) {
                            failCount.incrementAndGet();
                        }
                        return null;
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(); // 모든 스레드가 준비될 때까지 대기
        startLatch.countDown(); // 모든 스레드를 동시에 시작
        doneLatch.await(10, TimeUnit.SECONDS); // 최대 10초 대기
        executorService.shutdown();

        // Then
        assertTrue(successCount.get() >= 1 && successCount.get() <= 2, "1-2개의 업데이트가 성공해야 합니다.");
        assertTrue(failCount.get() >= 3 && failCount.get() <= 4, "3-4개의 업데이트는 실패해야 합니다.");

        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(Category.OUTER, updatedProduct.getCategory());
        assertTrue(updatedProduct.getBrand().startsWith("NewBrand"));
        assertTrue(updatedProduct.getPrice() >= 20000 && updatedProduct.getPrice() < 20000 + numberOfThreads);
    }

    @Test
    @DisplayName("재시도 로직 테스트")
    void retryLogicTest() throws InterruptedException {
        // Given
        Product product = productRepository.save(Product.builder()
                .category(Category.TOP)
                .brand("OriginalBrand")
                .price(10000)
                .build());

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger successCount = new AtomicInteger(0);

        // When
        Thread thread1 = new Thread(() -> {
            try {
                ProductUpdateCommand command = new ProductUpdateCommand(Category.OUTER, "NewBrand1", 20000);
                productService.update(product.getId(), command);
                successCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(100); // 첫 번째 스레드가 먼저 실행되도록 잠시 대기
                ProductUpdateCommand command = new ProductUpdateCommand(Category.PANTS, "NewBrand2", 30000);
                productService.update(product.getId(), command);
                successCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch.await(); // 두 스레드가 모두 작업을 마칠 때까지 대기

        // Then
        assertEquals(2, successCount.get(), "재시도 로직으로 인해 두 업데이트 모두 성공해야 합니다.");

        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(Category.PANTS, updatedProduct.getCategory());
        assertEquals("NewBrand2", updatedProduct.getBrand());
        assertEquals(30000, updatedProduct.getPrice());
    }


    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteProductTest() {
        // Given
        Product product = productRepository.save(Product.builder()
                .category(Category.TOP)
                .brand("DeleteBrand")
                .price(10000)
                .build());

        // When
        productService.delete(product.getId());

        // Then
        assertFalse(productRepository.existsById(product.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 예외 발생 테스트")
    void deleteNonExistentProductTest() {
        // When & Then
        assertThrows(CommonException.class, () -> productService.delete(999L));
    }

    @Test
    @DisplayName("가격이 0인 상품 추가 테스트")
    void addProductWithZeroPriceTest() {
        ProductAddCommand command = new ProductAddCommand(Category.TOP, "ZeroPriceBrand", 0);
        Long productId = productService.addProduct(command);

        Product savedProduct = productRepository.findById(productId).orElse(null);
        assert savedProduct != null;
        assertNotNull(savedProduct);
        assertEquals(0, savedProduct.getPrice());
    }

    @Test
    @DisplayName("가격이 매우 큰 상품 추가 테스트")
    void addProductWithLargePriceTest() {
        int largePrice = Integer.MAX_VALUE;
        ProductAddCommand command = new ProductAddCommand(Category.TOP, "LargePriceBrand", largePrice);
        Long productId = productService.addProduct(command);

        Product savedProduct = productRepository.findById(productId).orElse(null);
        assert savedProduct != null;
        assertNotNull(savedProduct);
        assertEquals(largePrice, savedProduct.getPrice());
    }

    @Test
    @DisplayName("모든 카테고리에 대한 최저가 상품 조회 테스트")
    void getCheapestProductsForAllCategoriesTest() {
        for (Category category : Category.values()) {
            productRepository.save(Product.builder().category(category).brand("Brand" + category).price(10000).build());
        }

        CheapestProductResponse response = productService.getCheapestProductsByCategory();
        assertEquals(Category.values().length, response.products().size());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 상품 추가 시도 시 예외 발생 테스트")
    void addProductWithNonExistentCategoryTest() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            ProductAddCommand command = new ProductAddCommand(null, "TestBrand", 10000);
            productService.addProduct(command);
        });
    }

    @Test
    @DisplayName("최저가와 최고가가 동일한 경우 처리 테스트")
    void getCategoryPriceRangeWithSamePricesTest() {
        productRepository.save(Product.builder().category(Category.TOP).brand("BrandA").price(10000).build());
        productRepository.save(Product.builder().category(Category.TOP).brand("BrandB").price(10000).build());

        CategoryPriceRangeResponse response = productService.getCategoryPriceRange("상의");
        assertEquals(response.lowestPrices().get(0).price(), response.highestPrices().get(0).price());
    }

    @Test
    @DisplayName("상품이 없을 때 최저가 브랜드 조회 테스트")
    void findLowestPriceBrandWithNoProductsTest() {
        assertThrows(CommonException.class, () -> productService.findLowestPriceBrand());
    }

    @Test
    @DisplayName("브랜드명이 빈 문자열일 때 상품 추가 테스트")
    void addProductWithEmptyBrandTest() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            ProductAddCommand command = new ProductAddCommand(Category.TOP, "", 10000);
            productService.addProduct(command);
        });
    }

    @Test
    @DisplayName("대량의 상품 데이터 처리 테스트")
    void handleLargeAmountOfProductsTest() {
        List<Product> largeProductList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            largeProductList.add(Product.builder()
                    .category(Category.values()[i % Category.values().length])
                    .brand("Brand" + i)
                    .price(1000 + i)
                    .build());
        }
        productRepository.saveAll(largeProductList);

        long startTime = System.currentTimeMillis();
        CheapestProductResponse response = productService.getCheapestProductsByCategory();
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 5000); // 5초 이내에 처리되어야 함
        assertEquals(Category.values().length, response.products().size());
    }
}

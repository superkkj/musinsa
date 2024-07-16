package com.dev.musinsa.controller;

import com.dev.musinsa.controller.request.ProductAddRequest;
import com.dev.musinsa.controller.request.ProductUpdateRequest;
import com.dev.musinsa.controller.response.*;
import com.dev.musinsa.database.entity.Product;
import com.dev.musinsa.database.repository.ProductRepository;
import com.dev.musinsa.database.type.Category;
import com.dev.musinsa.domain.ProductCollection;
import com.dev.musinsa.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        setUpTestData();
    }

    private void setUpTestData() {
        List<Product> products = Arrays.asList(
                Product.builder().category(Category.TOP).brand("BrandA").price(10000).build(),
                Product.builder().category(Category.TOP).brand("BrandB").price(11000).build(),
                Product.builder().category(Category.OUTER).brand("BrandA").price(5000).build(),
                Product.builder().category(Category.OUTER).brand("BrandB").price(5500).build(),
                Product.builder().category(Category.PANTS).brand("BrandA").price(3000).build(),
                Product.builder().category(Category.PANTS).brand("BrandB").price(3500).build(),
                Product.builder().category(Category.SNEAKERS).brand("BrandA").price(9000).build(),
                Product.builder().category(Category.SNEAKERS).brand("BrandB").price(9500).build(),
                Product.builder().category(Category.BAG).brand("BrandA").price(2000).build(),
                Product.builder().category(Category.BAG).brand("BrandB").price(2200).build(),
                Product.builder().category(Category.HAT).brand("BrandA").price(1500).build(),
                Product.builder().category(Category.HAT).brand("BrandB").price(1700).build(),
                Product.builder().category(Category.SOCKS).brand("BrandA").price(1800).build(),
                Product.builder().category(Category.SOCKS).brand("BrandB").price(2000).build(),
                Product.builder().category(Category.ACCESSORY).brand("BrandA").price(1800).build(),
                Product.builder().category(Category.ACCESSORY).brand("BrandB").price(1900).build()
        );
        productRepository.saveAll(products);
    }

    @Test
    @DisplayName("최저가 코디 조회 테스트")
    void getCheapestOutfitTest() {
        webTestClient
                .get()
                .uri("/api/products/cheapest-outfit")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<CheapestProductResponse>>() {
                })
                .value(response -> {
                    assertThat(response.resultCode()).isEqualTo(HttpStatus.OK.value());
                    CheapestProductResponse data = response.data();
                    assertThat(data.products()).hasSize(8);
                    assertThat(data.products()).extracting("category", "brand", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("상의", "BrandA", "10,000"),
                                    tuple("아우터", "BrandA", "5,000"),
                                    tuple("바지", "BrandA", "3,000"),
                                    tuple("스니커즈", "BrandA", "9,000"),
                                    tuple("가방", "BrandA", "2,000"),
                                    tuple("모자", "BrandA", "1,500"),
                                    tuple("양말", "BrandA", "1,800"),
                                    tuple("액세서리", "BrandA", "1,800")
                            );
                    assertThat(data.totalPrice()).isEqualTo("34,100");
                });

        // 데이터베이스와 비교 검증
        List<Product> cheapestProducts = productRepository.findCheapestProductsByCategory();
        assertThat(cheapestProducts).hasSize(8);
        assertThat(cheapestProducts).extracting("category", "brand", "price")
                .containsExactlyInAnyOrder(
                        tuple(Category.TOP, "BrandA", 10000),
                        tuple(Category.OUTER, "BrandA", 5000),
                        tuple(Category.PANTS, "BrandA", 3000),
                        tuple(Category.SNEAKERS, "BrandA", 9000),
                        tuple(Category.BAG, "BrandA", 2000),
                        tuple(Category.HAT, "BrandA", 1500),
                        tuple(Category.SOCKS, "BrandA", 1800),
                        tuple(Category.ACCESSORY, "BrandA", 1800)
                );
    }

    @Test
    @DisplayName("최저가 브랜드 조회 테스트")
    void getLowestPriceBrandTest() {
        webTestClient
                .get()
                .uri("/api/products/lowest-price-brand")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<LowestPriceBrandResponse>>() {
                })
                .value(response -> {
                    assertThat(response.resultCode()).isEqualTo(HttpStatus.OK.value());
                    LowestPriceBrandResponse data = response.data();
                    assertThat(data.brand()).isEqualTo("BrandA");
                    assertThat(data.categories()).hasSize(8);
                    assertThat(data.categories()).extracting("category", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("상의", "10,000"),
                                    tuple("아우터", "5,000"),
                                    tuple("바지", "3,000"),
                                    tuple("스니커즈", "9,000"),
                                    tuple("가방", "2,000"),
                                    tuple("모자", "1,500"),
                                    tuple("양말", "1,800"),
                                    tuple("액세서리", "1,800")
                            );
                    assertThat(data.totalPrice()).isEqualTo("34,100");
                });

        // 데이터베이스와 비교 검증
        BrandPriceSummary summary = productRepository.findLowestPriceBrandByAllCategories()
                .orElseThrow(() -> new AssertionError("Lowest price brand not found"));
        assertThat(summary.brand()).isEqualTo("BrandA");
        assertThat(summary.totalPrice()).isEqualTo(34100L);

        List<Product> products = productRepository.findProductsByBrand(summary.brand());
        assertThat(products).hasSize(8);
        assertThat(products).extracting("category", "price")
                .containsExactlyInAnyOrder(
                        tuple(Category.TOP, 10000),
                        tuple(Category.OUTER, 5000),
                        tuple(Category.PANTS, 3000),
                        tuple(Category.SNEAKERS, 9000),
                        tuple(Category.BAG, 2000),
                        tuple(Category.HAT, 1500),
                        tuple(Category.SOCKS, 1800),
                        tuple(Category.ACCESSORY, 1800)
                );

        // ProductCollection을 사용한 검증 (옵션)
        ProductCollection productCollection = new ProductCollection(products);
        List<CategoryPrice> categoryPrices = productCollection.getCategoryPrices();
        assertThat(categoryPrices).hasSize(8);
        assertThat(categoryPrices).extracting("category", "price")
                .containsExactlyInAnyOrder(
                        tuple("상의", "10,000"),
                        tuple("아우터", "5,000"),
                        tuple("바지", "3,000"),
                        tuple("스니커즈", "9,000"),
                        tuple("가방", "2,000"),
                        tuple("모자", "1,500"),
                        tuple("양말", "1,800"),
                        tuple("액세서리", "1,800")
                );
    }

    @Test
    @DisplayName("카테고리별 가격 범위 조회 테스트")
    void getCategoryPriceRangeTest() {
        webTestClient
                .get()
                .uri("/api/products/categories/상의/price-range")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<CategoryPriceRangeResponse>>() {
                })
                .value(response -> {
                    assertThat(response.resultCode()).isEqualTo(HttpStatus.OK.value());
                    CategoryPriceRangeResponse data = response.data();
                    assertThat(data.category()).isEqualTo("상의");
                    assertThat(data.lowestPrices()).hasSize(1);
                    assertThat(data.lowestPrices().get(0).brand()).isEqualTo("BrandA");
                    assertThat(data.lowestPrices().get(0).price()).isEqualTo("10,000");
                    assertThat(data.highestPrices()).hasSize(1);
                    assertThat(data.highestPrices().get(0).brand()).isEqualTo("BrandB");
                    assertThat(data.highestPrices().get(0).price()).isEqualTo("11,000");
                });

        // 데이터베이스와 비교 검증
        List<Product> products = productRepository.findMinMaxPriceProductsByCategory(Category.TOP);
        assertThat(products).hasSize(2);
        assertThat(products).extracting("brand", "price")
                .containsExactlyInAnyOrder(
                        tuple("BrandA", 10000),
                        tuple("BrandB", 11000)
                );
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void addProductTest() {
        //given
        ProductAddRequest request = new ProductAddRequest(Category.TOP, "TestBrand", 10000);


        //when then
        CommonResponse<Long> productId = webTestClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<Long>>() {
                })
                .returnResult()
                .getResponseBody();

        assert productId != null;
        productRepository.findById(productId.data()).ifPresent(product -> {
            assertThat(product.getCategory()).isEqualTo(Category.TOP);
            assertThat(product.getBrand()).isEqualTo("TestBrand");
            assertThat(product.getPrice()).isEqualTo(10000);
        });
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void updateProductTest() {

        // given
        ProductAddRequest addRequest = new ProductAddRequest(Category.TOP, "TestBrand", 10000);

        CommonResponse<Long> productId = webTestClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(addRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<Long>>() {
                })
                .returnResult()
                .getResponseBody();

        //when
        ProductUpdateRequest updateRequest = new ProductUpdateRequest(Category.OUTER, "UpdatedBrand", HttpStatus.OK.value());

        assert productId != null;
        webTestClient
                .put()
                .uri("/api/products/" + productId.data())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.resultCode")
                .isEqualTo(HttpStatus.OK.value());


        //then
        productRepository.findById(productId.data()).ifPresent(product -> {
            assertThat(product.getCategory()).isEqualTo(Category.OUTER);
            assertThat(product.getBrand()).isEqualTo("UpdatedBrand");
            assertThat(product.getPrice()).isEqualTo(HttpStatus.OK.value());
        });
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteProductTest() {

        // given
        ProductAddRequest addRequest = new ProductAddRequest(Category.TOP, "TestBrand", 10000);

        CommonResponse<Long> productId = webTestClient
                .post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(addRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CommonResponse<Long>>() {
                })
                .returnResult()
                .getResponseBody();


        //when
        assert productId != null;
        webTestClient
                .delete()
                .uri("/api/products/" + productId.data())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.resultCode")
                .isEqualTo(HttpStatus.OK.value());

        //then
        boolean present = productRepository.findById(productId.data()).isPresent();
        assertThat(present).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 조회 시 예외 처리 테스트")
    void getCategoryPriceRangeWithInvalidCategoryTest() {
        //given
        String invalidCategory = "InvalidCategory";

        //when then
        webTestClient
                .get()
                .uri("/api/products/categories/" + invalidCategory + "/price-range")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.resultCode").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.resultMsg").isEqualTo(ErrorCode.NOT_FOUND_CATEGORY.getMessage());

    }

    @Test
    @DisplayName("잘못된 상품 정보로 추가 시도 시 예외 처리 테스트")
    void addInvalidProductTest() {
        //given
        ProductAddRequest request = new ProductAddRequest(null, "", -1000);

        //when then
        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.resultCode")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("존재하지 않는 상품 수정 시도 시 예외 처리 테스트")
    void updateNonExistentProductTest() {
        //given
        ProductUpdateRequest request = new ProductUpdateRequest(Category.TOP, "Brand", 10000);
        Long invalidProductId = 9999L;
        //when then
        webTestClient.put()
                .uri("/api/products/" + invalidProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.resultCode").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시도 시 예외 처리 테스트")
    void deleteNonExistentProductTest() {
        //given
        Long invalidProductId = 9999L;

        //when then
        webTestClient.delete()
                .uri("/api/products/9999")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.resultCode").isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
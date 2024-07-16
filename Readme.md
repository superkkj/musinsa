# 무신사 코디 서비스

이 프로젝트는 무신사의 새로운 코디 서비스를 위한 백엔드 API를 구현 했습니다.

## 구현 범위

이 프로젝트에서는 다음 기능들을 구현했습니다.

1. 카테고리별 최저가 상품 조회 API
    - 주소: GET /api/products/cheapest-outfit
    - 설명: 각 카테고리별 최저가 상품과 총 가격을 조회합니다.

2. 단일 브랜드 전체 카테고리 최저가 조회 API
    - 주소: GET /api/products/lowest-price-brand
    - 설명: 모든 카테고리 상품을 가장 저렴하게 구매할 수 있는 브랜드와 총 가격을 조회합니다.

3. 카테고리별 최저가/최고가 브랜드와 가격 조회 API
    - 주소: GET /api/products/categories/{categoryName}/price-range
    - 설명: 특정 카테고리의 최저가와 최고가 브랜드 및 가격을 조회합니다.

4. 상품 관리 API
    - 추가: POST /api/products
    - 수정: PUT /api/products/{id}
    - 삭제: DELETE /api/products/{id}
    - 조회: GET /api/products
    - 설명: 상품의 추가, 수정, 삭제, 조회 기능을 제공합니다.

5. 주요 구현 특징:
    - Spring Boot를 사용한 RESTful API 구현
    - JPA를 이용한 데이터 접근 계층 구현
    - 트랜잭션 관리 및 동시성 제어(@Version을 이용한 낙관적 락)
    - 요청 데이터 유효성 검증(@Valid 사용)
    - 공통 응답 형식 사용 (CommonResponse)
    - 예외 처리 및 에러 코드 관리
    - 단위 테스트 및 통합 테스트 구현

6. 주요 구현 특징:
    - Java 17
    - Spring Boot 3.3.1
    - Spring Data JPA
    - H2 Database (개발 및 테스트용)
    - Gradle
    - JUnit 5
    - HTML, CSS, JavaScript를 이용한 간단한 프론트엔드 구현

### 빌드 방법
    ./gradlew build

### 테스트 실행 방법
    - ./gradlew test

### 애플리케이션 실행 방법
1. 애플리케이션 실행:
   - ./gradlew bootRun

2. 웹 브라우저에서 접속:
   - 프론트엔드 화면: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

### 애플리케이션 사용 방법
1. 프론트엔드 화면 사용:
   - 웹 브라우저에서 http://localhost:8080 에 접속합니다.
   - 화면에서 제공하는 버튼과 폼을 통해 API 기능을 사용할 수 있습니다.
   - 최저가 코디 조회, 최저가 브랜드 조회, 카테고리 가격 범위 조회 등의 기능을 이용할 수 있습니다.
   - 상품 추가, 수정, 삭제 기능도 화면에서 직접 사용 가능합니다.

2. Swagger UI를 통한 API 테스트:
   - 웹 브라우저에서 http://localhost:8080/swagger-ui.html 에 접속합니다.
   - Swagger UI에서 각 API의 상세 설명과 테스트 기능을 제공합니다.
   - "Try it out" 버튼을 클릭하여 각 API를 직접 테스트해볼 수 있습니다.
   - API 요청과 응답의 상세 내용을 확인할 수 있습니다.

3. 직접 API 호출:
   - RESTful API이므로 curl, Postman 등의 도구를 사용하여 직접 API를 호출할 수 있습니다.
   - API 엔드포인트와 요청 형식은 위의 '구현 범위' 섹션이나 Swagger UI에서 확인할 수 있습니다.

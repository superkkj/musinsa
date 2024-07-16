DROP TABLE IF EXISTS `products`;

CREATE TABLE products
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    category   VARCHAR(255) NOT NULL,
    brand      VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT check_brand_not_empty CHECK (brand != ''
) ,
    CONSTRAINT check_category_not_empty CHECK (category != '')
);

CREATE INDEX `idx_category_price` ON `products` (`category`, `price`);
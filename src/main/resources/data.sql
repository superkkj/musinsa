DROP TABLE IF EXISTS products;

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
INSERT INTO products (category, brand, price)
VALUES ('TOP', 'A', 11200),
       ('OUTER', 'A', 5500),
       ('PANTS', 'A', 4200),
       ('SNEAKERS', 'A', 9000),
       ('BAG', 'A', 2000),
       ('HAT', 'A', 1700),
       ('SOCKS', 'A', 1800),
       ('ACCESSORY', 'A', 2300),

       ('TOP', 'B', 10500),
       ('OUTER', 'B', 5900),
       ('PANTS', 'B', 3800),
       ('SNEAKERS', 'B', 9100),
       ('BAG', 'B', 2100),
       ('HAT', 'B', 2000),
       ('SOCKS', 'B', 2000),
       ('ACCESSORY', 'B', 2200),

       ('TOP', 'C', 10000),
       ('OUTER', 'C', 6200),
       ('PANTS', 'C', 3300),
       ('SNEAKERS', 'C', 9200),
       ('BAG', 'C', 2200),
       ('HAT', 'C', 1900),
       ('SOCKS', 'C', 2200),
       ('ACCESSORY', 'C', 2100),

       ('TOP', 'D', 10100),
       ('OUTER', 'D', 5100),
       ('PANTS', 'D', 3000),
       ('SNEAKERS', 'D', 9500),
       ('BAG', 'D', 2500),
       ('HAT', 'D', 1500),
       ('SOCKS', 'D', 2400),
       ('ACCESSORY', 'D', 2000),

       ('TOP', 'E', 10700),
       ('OUTER', 'E', 5000),
       ('PANTS', 'E', 3800),
       ('SNEAKERS', 'E', 9900),
       ('BAG', 'E', 2300),
       ('HAT', 'E', 1800),
       ('SOCKS', 'E', 2100),
       ('ACCESSORY', 'E', 2100),

       ('TOP', 'F', 11200),
       ('OUTER', 'F', 7200),
       ('PANTS', 'F', 4000),
       ('SNEAKERS', 'F', 9300),
       ('BAG', 'F', 2100),
       ('HAT', 'F', 1600),
       ('SOCKS', 'F', 2300),
       ('ACCESSORY', 'F', 1900),

       ('TOP', 'G', 10500),
       ('OUTER', 'G', 5800),
       ('PANTS', 'G', 3900),
       ('SNEAKERS', 'G', 9000),
       ('BAG', 'G', 2200),
       ('HAT', 'G', 1700),
       ('SOCKS', 'G', 2100),
       ('ACCESSORY', 'G', 2000),

       ('TOP', 'H', 10800),
       ('OUTER', 'H', 6300),
       ('PANTS', 'H', 3100),
       ('SNEAKERS', 'H', 9700),
       ('BAG', 'H', 2100),
       ('HAT', 'H', 1600),
       ('SOCKS', 'H', 2000),
       ('ACCESSORY', 'H', 2000),

       ('TOP', 'I', 11400),
       ('OUTER', 'I', 6700),
       ('PANTS', 'I', 3200),
       ('SNEAKERS', 'I', 9500),
       ('BAG', 'I', 2400),
       ('HAT', 'I', 1700),
       ('SOCKS', 'I', 1700),
       ('ACCESSORY', 'I', 2400);
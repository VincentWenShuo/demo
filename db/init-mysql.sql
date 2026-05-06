CREATE DATABASE IF NOT EXISTS demo
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE demo;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS stores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_stores_name UNIQUE (name)
);

INSERT INTO users (name, email)
SELECT 'Alice Tan', 'alice.tan@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'alice.tan@example.com'
);

INSERT INTO users (name, email)
SELECT 'Bob Lim', 'bob.lim@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'bob.lim@example.com'
);

INSERT INTO users (name, email)
SELECT 'Carol Lee', 'carol.lee@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'carol.lee@example.com'
);

INSERT INTO stores (name, address)
SELECT 'Main Store', '100 Market Street'
WHERE NOT EXISTS (
    SELECT 1 FROM stores WHERE name = 'Main Store'
);

INSERT INTO stores (name, address)
SELECT 'Harbour Store', '25 Harbour Road'
WHERE NOT EXISTS (
    SELECT 1 FROM stores WHERE name = 'Harbour Store'
);

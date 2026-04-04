-- GigShield - Parametric Insurance System
-- Run this against MySQL 8.x BEFORE starting the Spring Boot app
-- (ddl-auto=update will also auto-create, but this gives you explicit control)

CREATE DATABASE IF NOT EXISTS parametric_insurance
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parametric_insurance;

CREATE TABLE IF NOT EXISTS users (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone              VARCHAR(15)  NOT NULL UNIQUE,
    name               VARCHAR(100) NOT NULL,
    email              VARCHAR(150),
    city               VARCHAR(60)  NOT NULL,
    upi_id             VARCHAR(100) NOT NULL,
    platform           VARCHAR(20)  NOT NULL,
    platform_worker_id VARCHAR(60),
    tier               VARCHAR(10)  NOT NULL DEFAULT 'TIER_C',
    is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
    kyc_verified       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         DATETIME,
    updated_at         DATETIME,
    INDEX idx_city  (city),
    INDEX idx_phone (phone)
);

CREATE TABLE IF NOT EXISTS otp_sessions (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone      VARCHAR(15)  NOT NULL,
    otp_hash   VARCHAR(100) NOT NULL,
    expires_at DATETIME     NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at DATETIME,
    INDEX idx_phone_exp (phone, expires_at)
);

CREATE TABLE IF NOT EXISTS policies (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number     VARCHAR(30)    NOT NULL UNIQUE,
    user_id           BIGINT         NOT NULL,
    risk_type         VARCHAR(10)    NOT NULL,
    city              VARCHAR(60)    NOT NULL,
    ward              VARCHAR(60),
    status            VARCHAR(15)    NOT NULL DEFAULT 'ACTIVE',
    weekly_premium    INT            NOT NULL,
    payout_per_day    INT            NOT NULL,
    trigger_threshold DECIMAL(8,2)   NOT NULL,
    start_date        DATE,
    end_date          DATE,
    next_billing_date DATE,
    created_at        DATETIME,
    updated_at        DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_city_risk   (city, risk_type, status)
);

CREATE TABLE IF NOT EXISTS activity_logs (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT         NOT NULL,
    log_date       DATE           NOT NULL,
    active_hours   DECIMAL(4,2),
    deliveries     INT,
    platform_login BOOLEAN        DEFAULT FALSE,
    gps_city       VARCHAR(60),
    gps_ward       VARCHAR(60),
    gps_lat        DECIMAL(10,7),
    gps_lng        DECIMAL(10,7),
    created_at     DATETIME,
    UNIQUE KEY uq_user_date (user_id, log_date),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS trigger_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    city            VARCHAR(60)  NOT NULL,
    ward            VARCHAR(60),
    risk_type       VARCHAR(10)  NOT NULL,
    event_date      DATE         NOT NULL,
    observed_value  DECIMAL(10,2) NOT NULL,
    threshold_value DECIMAL(10,2) NOT NULL,
    data_source     VARCHAR(50)  NOT NULL,
    is_validated    BOOLEAN      DEFAULT FALSE,
    validation_note VARCHAR(255),
    created_at      DATETIME,
    UNIQUE KEY uq_city_risk_date (city, risk_type, event_date),
    INDEX idx_city_date (city, event_date)
);

CREATE TABLE IF NOT EXISTS claims (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_number     VARCHAR(30) NOT NULL UNIQUE,
    policy_id        BIGINT      NOT NULL,
    user_id          BIGINT      NOT NULL,
    trigger_event_id BIGINT      NOT NULL,
    claim_date       DATE,
    trigger_days     INT         DEFAULT 1,
    payout_amount    INT         NOT NULL,
    status           VARCHAR(15) NOT NULL DEFAULT 'PENDING',
    gps_verified     BOOLEAN,
    platform_active  BOOLEAN,
    fraud_score      DECIMAL(3,2),
    fraud_note       VARCHAR(500),
    settled_at       DATETIME,
    created_at       DATETIME,
    updated_at       DATETIME,
    FOREIGN KEY (policy_id)        REFERENCES policies(id),
    FOREIGN KEY (user_id)          REFERENCES users(id),
    FOREIGN KEY (trigger_event_id) REFERENCES trigger_events(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference_id   VARCHAR(40) NOT NULL UNIQUE,
    type           VARCHAR(10) NOT NULL,
    direction      VARCHAR(10) NOT NULL,
    entity_type    VARCHAR(10) NOT NULL,
    entity_id      BIGINT      NOT NULL,
    user_id        BIGINT      NOT NULL,
    amount         INT         NOT NULL,
    currency       VARCHAR(5)  NOT NULL DEFAULT 'INR',
    channel        VARCHAR(15) NOT NULL,
    channel_ref    VARCHAR(100),
    upi_id         VARCHAR(100),
    status         VARCHAR(15) NOT NULL DEFAULT 'INITIATED',
    attempt_count  INT         NOT NULL DEFAULT 1,
    failure_reason VARCHAR(500),
    is_reversed    BOOLEAN     DEFAULT FALSE,
    reversed_at    DATETIME,
    initiated_at   DATETIME,
    completed_at   DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_type (user_id, type),
    INDEX idx_status    (status)
);

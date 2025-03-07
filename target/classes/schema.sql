CREATE DATABASE IF NOT EXISTS library;
USE library;

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shifts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    location_id BIGINT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_armed BOOLEAN DEFAULT FALSE,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    shift_id BIGINT,
    assignment_date DATE NOT NULL,
    is_reten BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shift_id) REFERENCES shifts(id)
);

CREATE TABLE IF NOT EXISTS rest_schedules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    week_start_date DATE NOT NULL,
    is_two_day_rest BOOLEAN NOT NULL,
    rest_day1 DATE NOT NULL,
    rest_day2 DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_week_start (week_start_date)
);

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS library;
USE library;

-- Create locations table
CREATE TABLE IF NOT EXISTS locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    address VARCHAR(255)
);

-- Create shifts table
CREATE TABLE IF NOT EXISTS shifts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_id BIGINT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_armed BOOLEAN DEFAULT FALSE,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(100),
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- Create assignments table
CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shift_id BIGINT,
    assignment_date DATE NOT NULL,
    is_reten BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'PENDING',
    notes TEXT,
    FOREIGN KEY (shift_id) REFERENCES shifts(id)
);

-- Create rest_schedules table
CREATE TABLE IF NOT EXISTS rest_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rest_day1 DATE NOT NULL,
    rest_day2 DATE,
    is_two_day_rest BOOLEAN DEFAULT FALSE
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'NURSE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    birth_date DATE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Visits table
CREATE TABLE IF NOT EXISTS visits (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    doctor_id BIGINT NOT NULL REFERENCES users(id),
    visit_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    complaint TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drugs table
CREATE TABLE IF NOT EXISTS drugs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Prescriptions table
CREATE TABLE IF NOT EXISTS prescriptions (
    id BIGSERIAL PRIMARY KEY,
    visit_id BIGINT NOT NULL REFERENCES visits(id),
    drug_id BIGINT NOT NULL REFERENCES drugs(id),
    dosage VARCHAR(100) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed admin user (password: admin123)
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Seed doctor user (password: doctor123)
INSERT INTO users (username, password, role)
VALUES ('doctor1', '$2a$10$8K1p/a0dR1xqM2LpqZQ9aOqBjlV0Q5C7TGhFbJnNk/W7m3V6bXALe', 'DOCTOR')
ON CONFLICT (username) DO NOTHING;

-- Seed nurse user (password: nurse123)
INSERT INTO users (username, password, role)
VALUES ('nurse1', '$2a$10$Pl3N0p5WmrqhkfQJFZ0oGOqBjlV0Q5C7TGhFbJnNk/W7m3V6bXALe', 'NURSE')
ON CONFLICT (username) DO NOTHING;

-- Seed drugs
INSERT INTO drugs (name, stock, unit) VALUES ('阿莫西林', 100, '片') ON CONFLICT (name) DO NOTHING;
INSERT INTO drugs (name, stock, unit) VALUES ('布洛芬', 200, '片') ON CONFLICT (name) DO NOTHING;
INSERT INTO drugs (name, stock, unit) VALUES ('氯雷他定', 150, '片') ON CONFLICT (name) DO NOTHING;

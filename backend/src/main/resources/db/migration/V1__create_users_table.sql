CREATE TABLE users (
    id UUID PRIMARY KEY,

    first_name VARCHAR(50) NOT NULL,

    last_name VARCHAR(50) NOT NULL,

    email VARCHAR(255) NOT NULL,

    phone_number VARCHAR(20) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    status VARCHAR(30) NOT NULL,

    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    phone_verified BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_user_email UNIQUE (email),

    CONSTRAINT uk_user_phone_number UNIQUE (phone_number)
);

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_phone_number
    ON users(phone_number);

CREATE INDEX idx_users_status
    ON users(status);
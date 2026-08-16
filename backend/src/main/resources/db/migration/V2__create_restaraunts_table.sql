CREATE TABLE restaruants (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(300),
    phone_number VARCHAR(10) NOT NULL,
    logo_url VARCHAR(255),
    ratings NUMERIC(2, 1),
    total_ratings INTEGER,
    restaraunt_status VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);
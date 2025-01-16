CREATE TABLE IF NOT EXISTS users_profile (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    role_base VARCHAR(255),
    age INT
);

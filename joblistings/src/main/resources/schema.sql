CREATE TABLE IF NOT EXISTS "job-listings"(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description VARCHAR(255),
    company VARCHAR(100) NOT NULL,
    location VARCHAR(300),
    salary_range VARCHAR(250),
    experience_level VARCHAR(250),
    job_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS "application" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_listing_id BIGINT NOT NULL UNIQUE,
    applicant_id BIGINT NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    resume_url VARCHAR(255),
    cover_letter VARCHAR(1000),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
    );

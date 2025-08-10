-- Create users schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS users;

-- Create users table
CREATE TABLE users.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    is_account_non_expired BOOLEAN NOT NULL DEFAULT true,
    is_account_non_locked BOOLEAN NOT NULL DEFAULT true,
    is_credentials_non_expired BOOLEAN NOT NULL DEFAULT true,
    last_login_at TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

-- Create indexes
CREATE INDEX idx_users_username ON users.users(username) WHERE is_deleted = false;
CREATE INDEX idx_users_email ON users.users(email) WHERE is_deleted = false;
CREATE INDEX idx_users_role ON users.users(role) WHERE is_deleted = false;
CREATE INDEX idx_users_enabled ON users.users(is_enabled) WHERE is_deleted = false;
CREATE INDEX idx_users_created_at ON users.users(created_at);
CREATE INDEX idx_users_last_login ON users.users(last_login_at);

-- Create trigger for updating updated_at timestamp
CREATE OR REPLACE FUNCTION users.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users.users
    FOR EACH ROW
    EXECUTE FUNCTION users.update_updated_at_column();

-- Insert default admin user (password: admin123)
INSERT INTO users.users (
    username,
    email,
    password_hash,
    first_name,
    last_name,
    role,
    created_by
) VALUES (
    'admin',
    'admin@documind.ai',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewxjFr4dqDKrNwUy', -- admin123
    'System',
    'Administrator',
    'ADMIN',
    'system'
);

-- Add comments for documentation
COMMENT ON TABLE users.users IS 'User accounts for the DocuMind AI system';
COMMENT ON COLUMN users.users.password_hash IS 'BCrypt hashed password';
COMMENT ON COLUMN users.users.failed_login_attempts IS 'Number of consecutive failed login attempts';
COMMENT ON COLUMN users.users.locked_until IS 'Account locked until this timestamp';
COMMENT ON COLUMN users.users.is_deleted IS 'Soft delete flag';

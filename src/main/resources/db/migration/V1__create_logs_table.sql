-- Create logs table
CREATE TABLE IF NOT EXISTS logs (
    id BIGSERIAL PRIMARY KEY,
    service VARCHAR(100) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    level VARCHAR(20) NOT NULL,
    message TEXT,
    request_id VARCHAR(100) UNIQUE,
    latency_ms INTEGER,
    cpu_usage DECIMAL(5,4),
    endpoint VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
    );

-- Create indexes
CREATE INDEX idx_logs_service_timestamp
ON logs (service, timestamp DESC);

CREATE INDEX ids_logs_timestamp
ON logs (timestamp DESC);

CREATE INDEX idx_logs_request_id
ON logs (request_id);

CREATE INDEX idx_logs_level_timestamp
ON logs (level, timestamp DESC)
WHERE level IN ('ERROR', 'WARN');

-- Add comment
COMMENT ON TABLE logs IS 'Stores application log events';
-- 1. Create the new schema
CREATE SCHEMA IF NOT EXISTS observability_schema;

-- 2. Move the table (preserves all data, indexes, constraints)
ALTER TABLE public.logs SET SCHEMA observability_schema;

-- 3. Move the sequence too
ALTER SEQUENCE public.logs_id_seq SET SCHEMA observability_schema;
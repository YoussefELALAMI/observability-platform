DROP INDEX IF EXISTS ids_logs_timestamp;
CREATE INDEX IF NOT EXISTS idx_logs_timestamp ON public.logs USING btree ("timestamp" DESC);
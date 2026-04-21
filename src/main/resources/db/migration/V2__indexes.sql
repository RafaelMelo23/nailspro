CREATE UNIQUE INDEX IF NOT EXISTS uk_users_external_id_dtype ON users (external_id, dtype);
CREATE UNIQUE INDEX IF NOT EXISTS uk_salon_profile_tenant ON salon_profile (tenant_id);

CREATE INDEX IF NOT EXISTS idx_work_schedule_availability
    ON work_schedule (professional_id, day_of_week, start_time, end_time);

CREATE INDEX IF NOT EXISTS idx_appointment_booking_concurrency
    ON appointment (professional_id, appointment_status, start_date, end_date)
    WHERE (appointment_status NOT IN ('CANCELED', 'EXPIRED'));

CREATE INDEX IF NOT EXISTS idx_appointment_availability_range
    ON appointment (professional_id, start_date, end_date)
    WHERE (appointment_status IN ('CONFIRMED', 'FINISHED'));

CREATE INDEX IF NOT EXISTS idx_schedule_block_professional_range
    ON schedule_block (professional_id, start_time, end_time);
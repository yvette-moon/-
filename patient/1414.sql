ALTER TABLE queue_info DROP FOREIGN KEY queue_info_ibfk_1;
ALTER TABLE queue_info
ADD CONSTRAINT fk_queue_patient
FOREIGN KEY (patient_id) REFERENCES patient_info(patient_id);

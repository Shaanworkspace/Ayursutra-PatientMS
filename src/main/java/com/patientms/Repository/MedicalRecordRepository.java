package com.patientms.Repository;

import com.patientms.Entity.MedicalRecord;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientUserId(String patientId);
    List<MedicalRecord> findByDoctorId(String doctorId);
}
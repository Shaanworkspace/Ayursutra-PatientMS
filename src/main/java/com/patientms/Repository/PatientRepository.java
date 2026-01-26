package com.patientms.Repository;

import com.patientms.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    List<Patient> findByBloodGroup(String bloodGroup);


	boolean existsByUserId(String userId);

    Patient findByUserId(String userId);

	boolean existsByEmail(String email);

	Patient findByEmail(String email);
}
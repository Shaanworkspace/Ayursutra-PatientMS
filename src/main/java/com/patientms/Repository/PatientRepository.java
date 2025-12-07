package com.patientms.Repository;

import com.patientms.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Patient> findByBloodGroup(String bloodGroup);

    Boolean existsByEmail(String mail);
    Optional<Patient> findByKeycloakUserId(String keycloakUserId);

    Patient getByEmail(String email);
}
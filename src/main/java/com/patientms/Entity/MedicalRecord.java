package com.patientms.Entity;
import com.patientms.ENUM.MedicalRecordStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "medical_records")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String medicalRecordId;
    private String patientId;
    private String doctorId;
    private String doctorName;
    private String medications;
    private boolean needTherapy;
    private LocalDate visitDate;
    private LocalTime appointmentTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDateTime;
    private String allergies;

    @Column(length = 255)
    private String symptoms;

    private String prescribedTreatment;
    private MedicalRecordStatus sessionMedicalRecordStatus;
}

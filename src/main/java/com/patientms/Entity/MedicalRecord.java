package com.patientms.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate createdDate;

    private String allergies;

    @Column(length = 255)
    private String symptoms;

    private String prescribedTreatment;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

}

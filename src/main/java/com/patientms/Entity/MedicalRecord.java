package com.patientms.Entity;

import com.patientms.ENUM.TherapyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Patient patient;


    private String doctorId;
    private LocalDate visitDate;   // kab doctor ko dikhaaya
    private LocalDate createdDate;

    @Column(length = 255)
    private String symptoms;

    private String prescribedTreatment;

    /*
    This will update By Doctor
     */
    private String medications;
    private String followUpRequired;



    private String therapistId;

    /*
    Therapist update krega
     */
    private boolean approvedByTherapist;
    private boolean needTherapy;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TherapyType> therapies;

    private String therapistNotes;
    private String sessionStatus;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

}
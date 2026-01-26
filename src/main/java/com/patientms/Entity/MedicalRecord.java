package com.patientms.Entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.patientms.ENUM.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "medical_records")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Patient patient;


    private String doctorId;
    private LocalDate visitDate;   // kab doctor ko dikhaaya
    private LocalDate createdDate;

    @Column(length = 255)
    private String symptoms;
    @Lob
    private String prescribedTreatment;

    /*
    This will update By Doctor
     */
    @Lob
    private String medications;
    private String followUpRequired;
    private boolean needTherapy; // yes or no


    private String therapistId;  //Patient krega update

    /*
    Therapist update krega
     */
    private boolean approvedByTherapist;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
package com.patientms.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Patient {
    @Id
    private String userId;
    private String name;
    private Integer age;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;

    // Contact (optional profile info, not for login)
    private String phoneNumber;
    private String address;
    private String emergencyContact;

    // Only to check : Password,  Email stored for login , Not Mandatory
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer completedSessions;
    private Integer upcomingSessions;
    private Integer healthReportsCount;
    private Double wellnessScore;


    //Emergency
    private String emergencyName;
    private String emergencyRelation;
    private String emergencyPhone;


    //Body
    private Integer height;
    private Double weight;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MedicalRecord> medicalRecords = new ArrayList<>();


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
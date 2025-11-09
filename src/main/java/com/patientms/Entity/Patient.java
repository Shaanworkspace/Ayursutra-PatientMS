package com.patientms.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Profile
    @Column(nullable = false, length = 50)
    private String firstName;

    private Integer age=5;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 10)
    private String gender; // Male, Female, Other

    private LocalDate dateOfBirth;

    @Column(length = 5)
    private String bloodGroup; // A+, O-, etc.

    // Contact Information
    @Column(unique = true, nullable = false, length = 120)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    private String emergencyContact;

    // Authentication (hashed in real application)
    @Column(nullable = false)
    private String password;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    private List<Integer> medicalRecordsIds = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
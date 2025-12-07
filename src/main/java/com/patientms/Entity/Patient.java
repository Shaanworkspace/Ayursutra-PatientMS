package com.patientms.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String keycloakUserId;

    // Basic Profile
    private String firstName;
    private String lastName;
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
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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
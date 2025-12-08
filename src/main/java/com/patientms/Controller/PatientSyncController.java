package com.patientms.Controller;

import com.patientms.DTO.Request.UserCreatedEvent;
import com.patientms.Entity.Patient;
import com.patientms.Messaging.RabbitMQConfiguration;
import com.patientms.Repository.PatientRepository;

import com.patientms.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientSyncController {

    private final PatientRepository repo;
    private final JwtUtil jwtUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(HttpServletRequest request) {
        String keycloakId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(repo.existsByKeycloakUserId(keycloakId));
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncUser(HttpServletRequest request) {
        String keycloakId = jwtUtil.getUserIdFromRequest(request);

        if (repo.existsByKeycloakUserId(keycloakId)) {
            return ResponseEntity.ok("Already Exists");
        }

        Patient p = Patient.builder()
                .keycloakUserId(keycloakId)
                .build();

        repo.save(p);

        //  Publish event
        rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.USER_CREATED_QUEUE,
                new UserCreatedEvent(p.getId(), "PATIENT")
        );
        return ResponseEntity.ok("Created");
    }

}

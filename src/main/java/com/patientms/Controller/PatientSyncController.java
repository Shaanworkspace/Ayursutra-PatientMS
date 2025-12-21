package com.patientms.Controller;
import com.patientms.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientSyncController {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PATIENT SERVICE UP");
    }
    private final PatientRepository repo;


    @Autowired
    private RabbitTemplate rabbitTemplate;

}

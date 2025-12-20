package com.patientms.Controller;
import com.patientms.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientSyncController {

    private final PatientRepository repo;


    @Autowired
    private RabbitTemplate rabbitTemplate;

}

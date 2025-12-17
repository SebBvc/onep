package bvc.one.backend;

import bvc.one.backend.health.training.muscle.service.MuscleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MuscleService.class})
public class BackendApplication {

    static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}

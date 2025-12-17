package bvc.one.backend.health.training.muscle.config;

import bvc.one.backend.health.training.muscle.service.MuscleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule
public class MuscleConfig {

    @Bean
    public MuscleService muscleService() {
        return new MuscleService();
    }
}

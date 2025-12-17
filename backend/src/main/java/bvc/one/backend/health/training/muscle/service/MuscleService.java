package bvc.one.backend.health.training.muscle.service;

import bvc.one.backend.health.training.muscle.domain.MuscleCategory;
import org.springframework.cache.annotation.Cacheable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MuscleService {
    @Cacheable
    public Set<MuscleCategory> retrieveMuscleCategories() {
        return Arrays.stream(MuscleCategory.values()).collect(Collectors.toSet());
    }
}

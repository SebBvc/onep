package bvc.one.backend.health.training.muscle.service.dto;

import java.util.Set;

public record MuscleGroupDto(String name, Set<MuscleHeadDto> muscleHeads) {
}

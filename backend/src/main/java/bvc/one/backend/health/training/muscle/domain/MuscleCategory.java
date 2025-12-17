package bvc.one.backend.health.training.muscle.domain;

import java.util.Set;

public enum MuscleCategory {
    PECTORAL(MuscleGroup.CHEST),
    SHOULDER(MuscleGroup.SHOULDERS),
    ARM(MuscleGroup.BICEPS, MuscleGroup.TRICEPS),
    BACK(MuscleGroup.LOWER_BACK, MuscleGroup.MID_BACK, MuscleGroup.UPPER_BACK),
    LEG(MuscleGroup.QUADRICEPS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES, MuscleGroup.ADDUCTORS),
    CORE(MuscleGroup.CORE),
    HIP(MuscleGroup.GLUTES, MuscleGroup.HIP),
    FOREARM(MuscleGroup.FOREARMS),
    NECK(MuscleGroup.NECK);

    private final Set<MuscleGroup> groups;

    MuscleCategory(MuscleGroup... groups) {
        this.groups = Set.of(groups);
    }

    public Set<MuscleGroup> getGroups() {
        return groups;
    }

    public boolean contains(MuscleGroup group) {
        return groups.contains(group);
    }
}

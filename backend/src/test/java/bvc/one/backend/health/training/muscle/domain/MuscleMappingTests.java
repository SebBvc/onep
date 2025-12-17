package bvc.one.backend.health.training.muscle.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MuscleMappingTests {

    @Test
    void eachMuscleHeadIsInExactlyOneMuscleGroup() {
        for (MuscleHead head : MuscleHead.values()) {
            int count = 0;
            for (MuscleGroup group : MuscleGroup.values()) {
                if (group.contains(head)) {
                    count++;
                }
            }
            assertEquals(1, count, "MuscleHead " + head + " should be in exactly one MuscleGroup but was in " + count);
        }
    }

    @Test
    void eachMuscleGroupIsInExactlyOneMuscleCategory() {
        for (MuscleGroup group : MuscleGroup.values()) {
            int count = 0;
            for (MuscleCategory category : MuscleCategory.values()) {
                if (category.contains(group)) {
                    count++;
                }
            }
            assertEquals(1, count, "MuscleGroup " + group + " should be in exactly one MuscleCategory but was in " + count);
        }
    }
}


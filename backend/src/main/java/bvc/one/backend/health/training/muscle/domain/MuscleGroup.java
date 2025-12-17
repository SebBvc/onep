package bvc.one.backend.health.training.muscle.domain;

import java.util.Set;

public enum MuscleGroup {
    CHEST(MuscleHead.PECTORALIS_MAJOR_STERNAL, MuscleHead.PECTORALIS_MAJOR_CLAVICULAR),
    SHOULDERS(MuscleHead.ANTERIOR_DELTOID, MuscleHead.LATERAL_DELTOID, MuscleHead.POSTERIOR_DELTOID,
            MuscleHead.SUPRASPINATUS, MuscleHead.INFRASPINATUS, MuscleHead.TERES_MINOR, MuscleHead.SUBSCAPULARIS),
    BICEPS(MuscleHead.BICEPS_LONG_HEAD, MuscleHead.BICEPS_SHORT_HEAD),
    TRICEPS(MuscleHead.TRICEPS_LONG_HEAD, MuscleHead.TRICEPS_LATERAL_HEAD, MuscleHead.TRICEPS_MEDIAL_HEAD),
    LOWER_BACK(MuscleHead.ERECTOR_SPINAE, MuscleHead.LATISSIMUS_DORSI, MuscleHead.TERES_MAJOR),
    MID_BACK(MuscleHead.RHOMBOID_MAJOR, MuscleHead.RHOMBOID_MINOR, MuscleHead.SERRATUS_ANTERIOR),
    UPPER_BACK(MuscleHead.TRAPEZIUS_MIDDLE, MuscleHead.TRAPEZIUS_UPPER, MuscleHead.TRAPEZIUS_LOWER),
    QUADRICEPS(MuscleHead.RECTUS_FEMORIS, MuscleHead.VASTUS_LATERALIS, MuscleHead.VASTUS_MEDIALIS, MuscleHead.VASTUS_INTERMEDIUS),
    HAMSTRINGS(MuscleHead.BICEPS_FEMORIS_LONG, MuscleHead.BICEPS_FEMORIS_SHORT, MuscleHead.SEMITENDINOSUS, MuscleHead.SEMIMEMBRANOSUS),
    GLUTES(MuscleHead.GLUTEUS_MAXIMUS, MuscleHead.GLUTEUS_MEDIUS, MuscleHead.GLUTEUS_MINIMUS),
    CALVES(MuscleHead.GASTROCNEMIUS_MEDIAL, MuscleHead.GASTROCNEMIUS_LATERAL, MuscleHead.SOLEUS, MuscleHead.TIBIALIS_ANTERIOR),
    CORE(MuscleHead.RECTUS_ABDOMINIS, MuscleHead.EXTERNAL_OBLIQUE, MuscleHead.INTERNAL_OBLIQUE, MuscleHead.TRANSVERSUS_ABDOMINIS),
    ADDUCTORS(MuscleHead.ADDUCTOR_LONGUS, MuscleHead.ADDUCTOR_MAGNUS, MuscleHead.ADDUCTOR_BREVIS),
    HIP(MuscleHead.PIRIFORMIS, MuscleHead.PSOAS_MAJOR, MuscleHead.ILIACUS),
    FOREARMS(MuscleHead.BRACHIORADIALIS, MuscleHead.EXTENSOR_CARPI_RADIALIS_LONGUS, MuscleHead.EXTENSOR_CARPI_RADIALIS_BREVIS,
            MuscleHead.FLEXOR_CARPI_RADIALIS, MuscleHead.FLEXOR_CARPI_ULNARIS, MuscleHead.FLEXOR_DIGITORUM, MuscleHead.PRONATOR_TERES),
    NECK(MuscleHead.STERNOCLEIDOMASTOID, MuscleHead.LEVATOR_SCAPULAE, MuscleHead.SCALENES);

    private final Set<MuscleHead> heads;


    MuscleGroup(MuscleHead... heads) {
        this.heads = Set.of(heads);
    }

    public boolean contains(MuscleHead head) {
        return heads.contains(head);
    }
}

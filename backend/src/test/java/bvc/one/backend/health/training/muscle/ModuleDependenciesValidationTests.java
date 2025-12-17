package bvc.one.backend.health.training.muscle;

import bvc.one.backend.BackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;


class ModularityTests {

    ApplicationModules modules = ApplicationModules.of(BackendApplication.class);


    // TODO add more tests to verify specific module dependencies. Not so sure what ensuresHexagonal covers.
    @Test
    void verifiesModularStructure() {

        //var hexagonal = JMoleculesArchitectureRules.ensureOnionSimple();
        //var options = VerificationOptions.defaults().withAdditionalVerifications(hexagonal);
        modules.verify()
                .detectViolations()
                .throwIfPresent();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
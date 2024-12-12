package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuidesConfigurationTest {

    @Test
    void defaultValidateMetadata(GuidesConfiguration guidesConfiguration) {
        assertTrue(guidesConfiguration.isValidateMetadata());
    }

    @Test
    void defaultZipIncludesExtension(GuidesConfiguration guidesConfiguration) {
        assertEquals(List.of(".sh", ".bat"), guidesConfiguration.getZipIncludesExtensions());
    }
}

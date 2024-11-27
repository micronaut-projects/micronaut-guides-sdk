package io.micronaut.guides.cli;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideParser;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class FeaturesListingMacroSubstitutionTest {
    @Inject
    FeaturesListingMacroSubstitution featuresListingMacro;

    @Inject
    GuideParser guideParser;

    @Test
    void testSubstitute() {
        String content = "features-listing:default[]";

        String path = "src/test/resources";
        File file = new File(path);

        Optional<? extends Guide> metadata = guideParser.parseGuideMetadata(file, "metadata.json");
        String result = featuresListingMacro.substitute(content, metadata.get(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expected = """
                [source,bash]
                ----
                features: [app-name, graalvm, gradle, http-client-test, java, java-application, junit, logback, micronaut-aot, micronaut-build, micronaut-http-validation, netty-server, properties, readme, serialization-jackson, shade, static-resources]
                ----""";
        assertEquals(expected, result);
    }

}

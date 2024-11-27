package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class FeaturesWordsMacroSubstitutionTest {

    @Inject
    FeaturesWordsMacroSubstitution featuresWordsMacroSubstitution;

    @Test
    void testSubstitute(){
        App app = new App();
        app.setName("cli");
        app.setPackageName("example.micronaut");
        app.setApplicationType(ApplicationType.CLI);
        app.setFramework("Micronaut");
        app.setFeatures(List.of("yaml","mqtt"));
        Guide guide = new Guide();
        guide.setTitle("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API");
        guide.setIntro("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.");
        guide.setAuthors(List.of("Sergio del Amo"));
        guide.setCategories(List.of("Boot to Micronaut Building a REST API"));
        guide.setPublicationDate(LocalDate.of(2024,4,24));
        guide.setSlug("building-a-rest-api-spring-boot-vs-micronaut-data.adoc");
        guide.setLanguages(List.of(Language.JAVA));
        guide.setBuildTools(List.of(BuildTool.GRADLE));
        guide.setTestFramework(TestFramework.JUNIT);
        guide.setApps(List.of(app));
        String str = """
                Rename this `micronautguide` directory to `cli`.
                
                If you use Micronaut Launch, select Micronaut Application as application type and add @cli:features-words@ features.
                
                common:diff.adoc[]""";
        String result = featuresWordsMacroSubstitution.substitute(str, guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expected = """
                Rename this `micronautguide` directory to `cli`.
                
                If you use Micronaut Launch, select Micronaut Application as application type and add `yaml`, and `mqtt` features.
                
                common:diff.adoc[]""";
        assertEquals(expected, result);
    }
}

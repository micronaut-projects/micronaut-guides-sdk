package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.micronaut.guides.core.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class GuideProjectGeneratorTest {

    @Inject
    GuideParser guideParser;

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Test
    void testGenerate() throws IOException {
        File outputDirectory = Files.createTempDirectory("micronaut-guides").toFile();

        App app = new App();
        app.setName("cli");
        app.setPackageName("example.micronaut");
        app.setApplicationType(ApplicationType.CLI);
        app.setFramework("Micronaut");
        app.setFeatures(List.of("yaml", "mqtt"));
        Guide guide = new Guide();
        guide.setTitle("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API");
        guide.setIntro("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.");
        guide.setAuthors(List.of("Sergio del Amo"));
        guide.setCategories(List.of("Boot to Micronaut Building a REST API"));
        guide.setPublicationDate(LocalDate.of(2024,4,24));
        guide.setSlug("building-a-rest-api-spring-boot-vs-micronaut-data");
        guide.setLanguages(List.of(Language.JAVA));
        guide.setBuildTools(List.of(BuildTool.GRADLE));
        guide.setTestFramework(TestFramework.JUNIT);
        guide.setApps(List.of(app));

        assertDoesNotThrow(() -> guideProjectGenerator.generate(outputDirectory, guide));

        File dest = Paths.get(outputDirectory.getAbsolutePath(), MacroUtils.getSourceDir(guide.getSlug(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT))).toFile();

        assertTrue(new File(dest, "build.gradle").exists());
        assertTrue(new File(dest, "gradlew.bat").exists());
        assertTrue(new File(dest, "gradle.properties").exists());
        assertTrue(new File(dest, "gradlew").exists());
        assertTrue(new File(dest, "settings.gradle").exists());
        assertTrue(new File(dest, "micronaut-cli.yml").exists());
        assertTrue(new File(dest, "README.md").exists());
        //assertTrue(new File(dest, "LICENSEHEADER").exists());
        assertTrue(new File(dest, "src/main/resources/application.yml").exists());
        assertTrue(new File(dest, "src/main/resources/logback.xml").exists());
        assertTrue(new File(dest, "src/main/java/example/micronaut/CliCommand.java").exists());
        assertTrue(new File(dest, "src/test/java/example/micronaut/CliCommandTest.java").exists());

        // read the content of build.gradle
        File buildGradleFile = new File(dest, "build.gradle");
        String result = readFile(buildGradleFile);
        assertTrue(result.contains("""
                dependencies {
                    annotationProcessor("info.picocli:picocli-codegen")
                    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
                    implementation("info.picocli:picocli")
                    implementation("io.micronaut.mqtt:micronaut-mqttv5")
                    implementation("io.micronaut.picocli:micronaut-picocli")
                    implementation("io.micronaut.serde:micronaut-serde-jackson")
                    runtimeOnly("ch.qos.logback:logback-classic")
                    runtimeOnly("org.yaml:snakeyaml")
                }"""));
        assertTrue(result.contains("""
                micronaut {
                    testRuntime("junit5")
                    processing {
                        incremental(true)
                        annotations("example.micronaut.*")
                    }
                    testResources {
                        sharedServer = true
                    }
                }"""));
        assertTrue(result.contains("""
                application {
                    mainClass = "example.micronaut.CliCommand"
                }
                java {
                    sourceCompatibility = JavaVersion.toVersion("17")
                    targetCompatibility = JavaVersion.toVersion("17")
                }""") ||
                result.contains("""
                application {
                    mainClass = "example.micronaut.CliCommand"
                }
                java {
                    sourceCompatibility = JavaVersion.toVersion("21")
                    targetCompatibility = JavaVersion.toVersion("21")
                }"""));

    }

    @Test
    void testGenerateMultipleApps() {
        File outputDirectory = new File("build/tmp/test");
        outputDirectory.mkdir();

        String path = "src/test/resources/guides";
        File file = new File(path);

        List<? extends Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");
        Guide guide = metadatas.get(4);

        assertDoesNotThrow(() -> guideProjectGenerator.generate(outputDirectory, guide));

        for (App app : guide.getApps()) {
            File dest = Paths.get(outputDirectory.getAbsolutePath(), MacroUtils.getSourceDir(guide.getSlug(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)), app.getName()).toFile();
            assertTrue(new File(dest, "build.gradle").exists());
            assertTrue(new File(dest, "gradlew.bat").exists());
            assertTrue(new File(dest, "gradlew").exists());
            assertTrue(new File(dest, "settings.gradle").exists());
            //assertTrue(new File(dest, "src/main/java/example/micronaut/Application.java").exists());

            File buildGradleFile = new File(dest, "build.gradle");
            String result = readFile(buildGradleFile);

            for (String feature : app.getFeatures()) {
                //assertTrue(result.contains(feature));
            }
        }
    }


}

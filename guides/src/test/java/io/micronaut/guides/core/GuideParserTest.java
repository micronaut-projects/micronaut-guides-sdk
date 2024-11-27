package io.micronaut.guides.core;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class GuideParserTest {

    @Inject
    GuideParser guideParser;

    @Test
    void testParseGuidesMetadata() {
        String path = "src/test/resources/guides";
        File file = new File(path);

        List<? extends Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        assertEquals(5, metadatas.size());

        Guide guide = metadatas.get(1);
        assertEquals(List.of("Graeme Rocher"), guide.getAuthors());
        assertEquals("Connect a Micronaut Data JDBC Application to Azure Database for MySQL", guide.getTitle());
        assertEquals("Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL", guide.getIntro());
        assertEquals(List.of("Data JDBC"), guide.getCategories());
        assertEquals(LocalDate.of(2022, 2, 17), guide.getPublicationDate());
        assertEquals("base", guide.getBase());
        assertEquals("child", guide.getSlug());
        assertEquals(List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN), guide.getLanguages());
        assertEquals(List.of(BuildTool.GRADLE, BuildTool.MAVEN), guide.getBuildTools());
        assertTrue(guide.getZipIncludes().isEmpty());
        assertTrue(guide.getEnv().isEmpty());
        List<String> tags = guide.getTags();
        Collections.sort(tags);
        assertEquals(List.of("Azure", "cloud", "data-jdbc", "database", "flyway", "jdbc", "micronaut-data", "mysql"), tags);
        List<App> apps = (List<App>) guide.getApps();
        assertNotNull(apps);
        assertEquals(1, apps.size());
        assertTrue(apps.stream().anyMatch(app -> app.getName().equals("default") &&
                app.getApplicationType() == ApplicationType.DEFAULT &&
                app.getPackageName().equals("example.micronaut") &&
                app.getFramework().equals("Micronaut") &&
                app.getFeatures().isEmpty() &&
                app.invisibleFeatures().isEmpty() &&
                app.getKotlinFeatures().isEmpty() &&
                app.getJavaFeatures().isEmpty() &&
                app.getGroovyFeatures().isEmpty() &&
                app.getTestFramework() == null &&
                app.getExcludeTest() == null &&
                app.getExcludeSource() == null &&
                !app.isValidateLicense()));

        guide = metadatas.get(4);
        assertEquals(List.of("Sergio del Amo"), guide.getAuthors());
        assertEquals("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API", guide.getTitle());
        assertEquals("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.", guide.getIntro());
        assertEquals(List.of("spring-boot-starter-web", "jackson-databind", "spring-boot", "assertj", "boot-to-micronaut-building-a-rest-api", "json-path"), guide.getTags());
        assertEquals(List.of("Boot to Micronaut Building a REST API"), guide.getCategories());
        assertEquals(LocalDate.of(2024, 4, 24), guide.getPublicationDate());
        assertEquals(List.of(Language.JAVA), guide.getLanguages());
        assertEquals(List.of(BuildTool.GRADLE), guide.getBuildTools());
        apps = (List<App>) guide.getApps();
        assertNotNull(apps);
        assertEquals(3, apps.size());
        assertTrue(apps.stream().anyMatch(app -> app.getName().equals("springboot") &&
                app.getApplicationType() == ApplicationType.DEFAULT &&
                app.getPackageName().equals("example.micronaut") &&
                app.getFramework().equals("Spring Boot") &&
                app.getFeatures().equals(List.of("spring-boot-starter-web")) &&
                app.invisibleFeatures().isEmpty() &&
                app.getKotlinFeatures().isEmpty() &&
                app.getJavaFeatures().isEmpty() &&
                app.getGroovyFeatures().isEmpty() &&
                app.getTestFramework() == null &&
                app.getExcludeTest() == null &&
                app.getExcludeSource() == null &&
                !app.isValidateLicense()));
        assertTrue(apps.stream().anyMatch(app -> app.getName().equals("micronautframeworkjacksondatabind") &&
                app.getApplicationType() == ApplicationType.DEFAULT &&
                app.getPackageName().equals("example.micronaut") &&
                app.getFramework().equals("Micronaut") &&
                app.getFeatures().equals(List.of("json-path", "assertj", "jackson-databind")) &&
                app.invisibleFeatures().isEmpty() &&
                app.getKotlinFeatures().isEmpty() &&
                app.getJavaFeatures().isEmpty() &&
                app.getGroovyFeatures().isEmpty() &&
                app.getTestFramework() == null &&
                app.getExcludeTest() == null &&
                app.getExcludeSource() == null &&
                !app.isValidateLicense()));
        assertTrue(apps.stream().anyMatch(app -> app.getName().equals("micronautframeworkserde") &&
                app.getApplicationType() == ApplicationType.DEFAULT &&
                app.getPackageName().equals("example.micronaut") &&
                app.getFramework().equals("Micronaut") &&
                app.getFeatures().equals(List.of("json-path", "assertj")) &&
                app.invisibleFeatures().isEmpty() &&
                app.getKotlinFeatures().isEmpty() &&
                app.getJavaFeatures().isEmpty() &&
                app.getGroovyFeatures().isEmpty() &&
                app.getTestFramework() == null &&
                app.getExcludeTest() == null &&
                app.getExcludeSource() == null &&
                !app.isValidateLicense()));
        assertFalse(guide.isSkipGradleTests());
        assertFalse(guide.isSkipMavenTests());
        assertNull(guide.getMinimumJavaVersion());
        assertNull(guide.getMaximumJavaVersion());
        assertNull(guide.getCloud());
        assertTrue(guide.isPublish());
        assertEquals("test.adoc", guide.getAsciidoctor());
        assertEquals("test", guide.getSlug());
        assertTrue(guide.getZipIncludes().isEmpty());
        assertNull(guide.getBase());
        assertTrue(guide.getEnv().isEmpty());
    }
}

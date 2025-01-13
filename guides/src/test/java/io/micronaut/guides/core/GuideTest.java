package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.json.JsonMapper;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

    @Inject
    DefaultGuideMerger guideMerger;

    @Test
    void testGuideWithBase() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:base.json");
        assertTrue(inputStreamOptional.isPresent());
        final InputStream inputStreamBase = inputStreamOptional.get();
        Guide base = assertDoesNotThrow(() -> jsonMapper.readValue(inputStreamBase, Guide.class));
        for (App app : base.getApps()) {
            if (StringUtils.isEmpty(app.getPackageName())) {
                app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
            }
        }
        inputStreamOptional = resourceLoader.getResourceAsStream("classpath:child.json");
        assertTrue(inputStreamOptional.isPresent());
        final InputStream inputStreamChild = inputStreamOptional.get();
        Guide child = assertDoesNotThrow(() -> jsonMapper.readValue(inputStreamChild, Guide.class));
        for (App app : child.getApps()) {
            if (StringUtils.isEmpty(app.getPackageName())) {
                app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
            }
        }
        guideMerger.merge(base, child);
        assertEquals(List.of("Graeme Rocher"), child.getAuthors());
        assertEquals("Connect a Micronaut Data JDBC Application to Azure Database for MySQL", child.getTitle());
        assertEquals("Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL", child.getIntro());
        assertEquals(List.of("Data JDBC"), child.getCategories());
        assertEquals(LocalDate.of(2022, 2, 17), child.getPublicationDate());
        List<String> tags = child.getTags();
        Collections.sort(tags);
        assertEquals(List.of("Azure", "cloud", "data-jdbc", "database", "flyway", "jdbc", "micronaut-data", "mysql"), tags);
        List<App> apps = (List<App>) child.getApps();
        assertNotNull(apps);
        assertEquals(1, apps.size());
        assertTrue(apps.stream().anyMatch(app -> {
            return app.getName().equals("default") &&
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
                    !app.isValidateLicense();
        }));
    }

    @Test
    void typeCloudCanBeNull() {
        String title = "1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API";
        String intro = "This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.";
        List<String> categories = List.of("Boot to Micronaut Building a REST API");
        List<String> authors = List.of("Sergio del Amo");
        LocalDate publicationDate = LocalDate.of(2024, 4, 24);
        List<App> apps = new ArrayList<>();
        App app = new App();
        app.setName("springboot");
        apps.add(app);
        Guide guide = new Guide();
        guide.setTitle(title);
        guide.setIntro(intro);
        guide.setAuthors(authors);
        guide.setCategories(categories);
        guide.setPublicationDate(publicationDate);
        guide.setApps(apps);
        Set<ConstraintViolation<Guide>> violations = validator.validate(guide);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testDeserialization() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        for (App app : guide.getApps()) {
            if (StringUtils.isEmpty(app.getPackageName())) {
                app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
            }
        }
        assertEquals(List.of("Sergio del Amo"), guide.getAuthors());
        assertEquals("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API", guide.getTitle());
        assertEquals("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.", guide.getIntro());
        assertEquals(List.of("spring-boot-starter-web", "jackson-databind", "spring-boot", "assertj", "boot-to-micronaut-building-a-rest-api", "json-path"), guide.getTags());
        assertEquals(List.of("Boot to Micronaut Building a REST API"), guide.getCategories());
        assertEquals(LocalDate.of(2024, 4, 24), guide.getPublicationDate());
        assertEquals(List.of(Language.JAVA), guide.getLanguages());
        assertEquals(List.of(BuildTool.GRADLE), guide.getBuildTools());
        List<App> apps = (List<App>) guide.getApps();
        assertNotNull(apps);
        assertEquals(3, apps.size());
        assertTrue(apps.stream().anyMatch(app -> {
            return app.getName().equals("springboot") &&
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
                    !app.isValidateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.getName().equals("micronautframeworkjacksondatabind") &&
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
                    !app.isValidateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.getName().equals("micronautframeworkserde") &&
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
                    !app.isValidateLicense();
        }));
        assertFalse(guide.isSkipGradleTests());
        assertFalse(guide.isSkipMavenTests());
        assertNull(guide.getMinimumJavaVersion());
        assertNull(guide.getMaximumJavaVersion());
        assertNull(guide.getCloud());
        assertNull(guide.getAsciidoctor());
        assertTrue(guide.isPublish());
        assertNull(guide.getAsciidoctor());
        assertNull(guide.getSlug());
        assertTrue(guide.getZipIncludes().isEmpty());
        assertNull(guide.getBase());
        assertTrue(guide.getEnv().isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
    }

    @Test
    void typeGuideIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void typeGuideIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void defaultValuesAreSetCorrectly() {
        Guide guide = new Guide();
        assertEquals(guide.isPublish(), true);
    }
}

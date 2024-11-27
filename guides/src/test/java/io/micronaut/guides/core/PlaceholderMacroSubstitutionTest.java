package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.util.VersionInfo;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class PlaceholderMacroSubstitutionTest {

    @Inject
    PlaceholderMacroSubstitution placeholderMacroSubstitution;

    private Guide guide;
    private GuidesOption option;

    @BeforeEach
    void setup() {
        guide = new Guide();
        guide.setTitle("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API");
        guide.setIntro("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.");
        guide.setAuthors(List.of("Sergio del Amo"));
        guide.setCategories(List.of("Boot to Micronaut Building a REST API"));
        guide.setPublicationDate(LocalDate.of(2024,4,24));
        guide.setSlug("building-a-rest-api-spring-boot-vs-micronaut-data");
        guide.setAsciidoctor("building-a-rest-api-spring-boot-vs-micronaut-data.adoc");
        guide.setLanguages(List.of(Language.JAVA));
        guide.setBuildTools(List.of(BuildTool.GRADLE));
        guide.setTestFramework(TestFramework.JUNIT);
        option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
    }

    @Test
    void testSubstitute() {
        String str = """
                = @guideTitle@

                @guideIntro@

                Authors: @authors@

                Language: @language@ (@lang@)

                Build: @build@

                Test Framework: @testFramework@

                Minimum JDK: @minJdk@

                API: @api@

                File: @sourceDir@/Main.@languageextension@

                Test File: @sourceDir@/Main@testsuffix@.@languageextension@
                """;
        String result = placeholderMacroSubstitution.substitute(str, guide, option);
        String expected = """
                = 1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API

                This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.

                Authors: Sergio del Amo

                Language: Java (java)

                Build: gradle

                Test Framework: junit

                Minimum JDK: 17

                API: https://docs.micronaut.io/latest/api

                File: building-a-rest-api-spring-boot-vs-micronaut-data-gradle-java/Main.java

                Test File: building-a-rest-api-spring-boot-vs-micronaut-data-gradle-java/MainTest.java
                """;
        assertEquals(expected, result);
    }

    @Test
    void testSubstituteVersion(){
        String str = """
                @micronaut@""";
        String result = placeholderMacroSubstitution.substitute(str, guide, option);
        assertNotEquals("@micronaut@", result);
    }

    @Test
    void testSubstituteMicronautVersion(){
        String str = """
                @micronautVersion@""";
        String result = placeholderMacroSubstitution.substitute(str, guide, option);
        assertEquals(VersionInfo.getMicronautVersion(),result);
    }

    @Test
    void testSubstitutionCoordinates(){
        String str = """
                dependency:log4j-core[groupId=org.apache.logging.log4j,scope=implementation,version=@log4j-coreVersion@]
                """;
        String result = placeholderMacroSubstitution.substitute(str, guide, option);
        assertNotEquals(str, result);
    }
}

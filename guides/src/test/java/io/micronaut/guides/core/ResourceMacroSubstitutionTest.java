package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class ResourceMacroSubstitutionTest {

    @Inject
    ResourceMacroSubstitution resourceMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = "resource:../../../ttfr.sh[]";
        String resJava = resourceMacroSubstitution.substitute(str, new GuideRender(GuideTestUtils.guideWithSlug("executable-jar"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)));
        String expectedJava = """
                [source,sh]
                .ttfr.sh
                ----
                include::{sourceDir}/executable-jar/executable-jar-gradle-java/src/main/resources/../../../ttfr.sh[]
                ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void testSubstituteWithTags() {
        String str = "resource:application.yml[tag=endpoints]";
        String resJava = resourceMacroSubstitution.substitute(str, new GuideRender(GuideTestUtils.guideWithSlug("adding-commit-info"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)));
        String expectedJava = """
                [source,yaml]
                .src/main/resources/application.yml
                ----
                include::{sourceDir}/adding-commit-info/adding-commit-info-gradle-java/src/main/resources/application.yml[tag=endpoints]
                ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void testMultiLineInput(){
        String str = """
                The `git.properties` file that is generated by the `gradle-git-properties` plugin
                will not be accessible from the native executable unless access to the file is
                configured in `resource-config.json`:
                
                resource:application.yml[tag=endpoints]
                
                resource:test.yml[tag=security,app=testApp]
                """;
        String resJava = resourceMacroSubstitution.substitute(str, new GuideRender(GuideTestUtils.guideWithSlug("adding-commit-info"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)));
        String expectedJava = """
                The `git.properties` file that is generated by the `gradle-git-properties` plugin
                will not be accessible from the native executable unless access to the file is
                configured in `resource-config.json`:
                
                [source,yaml]
                .src/main/resources/application.yml
                ----
                include::{sourceDir}/adding-commit-info/adding-commit-info-gradle-java/src/main/resources/application.yml[tag=endpoints]
                ----
                
                [source,yaml]
                .testApp/src/main/resources/test.yml
                ----
                include::{sourceDir}/adding-commit-info/adding-commit-info-gradle-java/testApp/src/main/resources/test.yml[tag=security]
                ----
                """;
        assertEquals(expectedJava, resJava);
    }
}
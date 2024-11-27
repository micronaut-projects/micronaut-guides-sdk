package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class TestScriptGeneratorTest {

    @Inject
    GuideParser guideParser;

    @Inject
    TestScriptGenerator testScriptGenerator;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void supportsNativeTestAllConditions() {
        App app = new App();
        app.setName("springboot");
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertTrue(result);
    }

    @Test
    void supportsNativeTestNotMicronaut() {
        App app = new App();
        app.setName("app");
        app.setFramework("Spring");
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotGradle() {
        App app = new App();
        app.setName("springboot");
        app.setFramework("Micronaut");
        GuidesOption guidesOption = new GuidesOption(MAVEN, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotLanguage() {
        App app = new App();
        app.setName("springboot");
        app.setFramework("Micronaut");
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.GROOVY, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotJUnit() {
        App app = new App();
        app.setName("springboot");
        app.setFramework("Micronaut");
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.SPOCK);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNullFramework() {
        App app = new App();
        app.setName("springboot");
        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsMicronaut() {
        App app = new App();
        app.setName("springboot");
        app.setFramework("Micronaut");
        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsNotMicronaut() {
        App app = new App();
        app.setName("app");
        app.setFramework("Spring");
        assertFalse(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsGroovy() {
        assertFalse(testScriptGenerator.supportsNativeTest(Language.GROOVY));
    }

    @Test
    void testGenerate() {
        String path = "src/test/resources/guides";
        File file = new File(path);
        List<? extends Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        File expectedFile = new File(resourceLoader.getResource("classpath:expected_test_script.sh").orElseThrow().getFile());
        String expected = TestUtils.readFile(expectedFile);

        String result = testScriptGenerator.generateTestScript(metadatas);

        assertEquals(expected.strip(), result.strip());
    }

    @Test
    void testGenerateNative() {
        String path = "src/test/resources/guides";
        File file = new File(path);
        List<? extends Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        File expectedFile = new File(resourceLoader.getResource("classpath:expected_test_script_native.sh").orElseThrow().getFile());
        String expected = TestUtils.readFile(expectedFile);

        String result = testScriptGenerator.generateNativeTestScript(metadatas);

        assertEquals(expected.strip(), result.strip());
    }
}

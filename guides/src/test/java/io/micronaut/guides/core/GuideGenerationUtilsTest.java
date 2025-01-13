package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
class GuideGenerationUtilsTest {
    private static final Logger LOG = LoggerFactory.getLogger(GuideGenerationUtilsTest.class);

    @Inject
    GuidesConfiguration configuration;

    @Test
    void testMainPath() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        App app = new App();
        app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
        String result = GuideGenerationUtils.mainPath(app, "fileName", option, configuration);

        assertEquals("src/main/java/example/micronaut/fileName.java", result);
    }

    @Test
    void testTestPath() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        App app = new App();
        app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
        String result = GuideGenerationUtils.testPath(app, "fileNameTest", option, configuration);

        assertEquals("src/test/java/example/micronaut/fileNameTest.java", result);
    }


    @Test
    void testPathByFolder() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        App app = new App();
        app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);

        String result = GuideGenerationUtils.pathByFolder(app, "fileName", "main", option);

        assertEquals("src/main/java/example/micronaut/fileName.java", result);
    }

    @Test
    void testGuidesOptions() {
        Guide guideMetadata = new Guide();
        guideMetadata.setLanguages(List.of(Language.JAVA, Language.KOTLIN));
        guideMetadata.setBuildTools(List.of(BuildTool.GRADLE, BuildTool.MAVEN));
        guideMetadata.setTestFramework(TestFramework.JUNIT);
        List<GuidesOption> result = GuideGenerationUtils.guidesOptions(guideMetadata, LOG);

        assertEquals(4, result.size());
    }

    @Test
    void testTestFrameworkOption() {
        assertEquals(TestFramework.SPOCK, GuideGenerationUtils.testFrameworkOption(Language.GROOVY, null));
        assertEquals(TestFramework.JUNIT, GuideGenerationUtils.testFrameworkOption(Language.JAVA, null));
        assertEquals(TestFramework.SPOCK, GuideGenerationUtils.testFrameworkOption(Language.JAVA, TestFramework.SPOCK));
    }

    @Test
    void testResolveJdkVersion() {
        JdkVersion result = GuideGenerationUtils.resolveJdkVersion(configuration);

        assertNotNull(result);
    }
}

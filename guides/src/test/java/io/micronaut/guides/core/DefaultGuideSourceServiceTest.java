package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class DefaultGuideSourceServiceTest {
    @Test
    void testGetSourceDir(GuideSourceService guideSourceService) {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        Guide guide = new Guide();
        guide.setSlug("slug");
        String result = guideSourceService.guideSourceFolder(guide, option);
        assertEquals("slug-gradle-java", result);
    }

}

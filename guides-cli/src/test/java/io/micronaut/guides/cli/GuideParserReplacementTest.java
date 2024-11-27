package io.micronaut.guides.cli;

import io.micronaut.guides.core.Guide;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideParserReplacementTest {

    @Inject
    GuideParserReplacement guideParserReplacement;

    @Test
    void testParseGuidesMetadata() {
        String path = "src/test/resources";
        File file = new File(path);

        Optional<? extends Guide> metadata = guideParserReplacement.parseGuideMetadata(file, "metadata.json");
        assertTrue(metadata.isPresent());
        assertInstanceOf(GdkGuide.class, metadata.get());
        GdkGuide guide = (GdkGuide) metadata.get();
        assertInstanceOf(GdkApp.class, guide.getApps().get(0));
        assertEquals(false, guide.skipCodeSamples());
        assertEquals(List.of(BuildTool.GRADLE, BuildTool.MAVEN), guide.getBuildTools());
        assertEquals(List.of(Language.JAVA), guide.getLanguages());
        assertEquals(List.of("Database"), guide.getCategories());
        assertEquals(List.of("database", "graalvm"), guide.getTags());
        GdkApp gdkApp = (GdkApp) guide.getApps().get(0);
        assertEquals(List.of("database"), gdkApp.getServices());
    }
}

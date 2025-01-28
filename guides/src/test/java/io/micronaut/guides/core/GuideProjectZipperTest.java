package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class GuideProjectZipperTest {

    @Inject
    GuideProjectZipper guideProjectZipper;

    @Inject
    GuideParser guideParser;

    //TODO: fix by zipping an actual guide
    @Test
    @Disabled
    void testZip() throws IOException {
        String projectDir = "src/test/resources/";
        String outputDir = "build/tmp/test";

        Guide guide = guideParser.parseGuidesMetadata(new File(projectDir)).stream().filter(g -> g.getSlug().equals("creating-your-first-micronaut-app")).findFirst().get();
        GuidesOption guidesOption = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);

        guideProjectZipper.zipGuide(guide, new File(outputDir));

        List<String> expected = List.of("creating-your-first-micronaut-app.adoc", "metadata.json");
        List<String> result = new LinkedList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(outputDir, guideProjectZipper.getZipFileName(guide, guidesOption))))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                result.add(zipEntry.getName());
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }

        Collections.sort(result);

        assertEquals(expected, result);
    }
}

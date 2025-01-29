package io.micronaut.guides.core;

import io.micronaut.guides.core.zip.GuideProjectZipper;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Test
    void testZip() throws IOException {
        File outputDirectory = Files.createTempDirectory("micronaut-guides").toFile();

        List<Guide> metadatas = new ArrayList<>();

        String path = "src/test/resources/other-guides/adding-commit-info";
        File file = new File(path);
        guideParser.parseGuideMetadata(file, "metadata.json").ifPresent(metadatas::add);

        guideProjectGenerator.generate(outputDirectory, metadatas.get(0));

        GuidesOption guidesOption = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);

        guideProjectZipper.zipGuide(metadatas.get(0), outputDirectory);

        List<String> expected = List.of(
                ".gitignore",
                "README.md",
                "build.gradle",
                "gradle.properties",
                "gradle/wrapper/gradle-wrapper.jar",
                "gradle/wrapper/gradle-wrapper.properties",
                "gradlew",
                "gradlew.bat",
                "micronaut-cli.yml",
                "settings.gradle",
                "src/main/java/example/micronaut/Application.java",
                "src/main/resources/application.yml",
                "src/main/resources/logback.xml",
                "src/test/java/example/micronaut/DefaultTest.java"
        );
        List<String> result = new LinkedList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(outputDirectory, guideProjectZipper.getZipFileName(metadatas.get(0), guidesOption) + ".zip")))) {
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

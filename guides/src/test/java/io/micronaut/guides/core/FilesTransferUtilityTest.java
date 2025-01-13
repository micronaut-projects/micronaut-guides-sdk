package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.guides.core.DefaultFilesTransferUtility.pathByFolder;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class FilesTransferUtilityTest {

    @Inject
    FilesTransferUtility filesTransferUtility;

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Inject
    GuideParser guideParser;

    @Inject
    GuidesConfiguration guidesConfiguration;

    @Test
    void testPathByFolder() {
        App app = new App();
        app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
        app.setName("books");
        String source = "HelloController";
        String pathType = "main";
        GuidesOption guidesOption = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String path = pathByFolder(app, source, pathType, guidesOption);

        String base = "guides/micronaut-example/micronaut-example-gradle-java/books";

        assertEquals("guides/micronaut-example/micronaut-example-gradle-java/books/src/main/java/example/micronaut/HelloController.java", Path.of(base, path).toString());

        String oldPath = pathType.equals("main") ? GuideGenerationUtils.mainPath(app, source, guidesOption, guidesConfiguration) : GuideGenerationUtils.testPath(app, source, guidesOption, guidesConfiguration);

        assertNotEquals(path, Path.of(base, oldPath).toString());
    }

    @Test
    void testTransfer() throws Exception {
        File outputDirectory = Files.createTempDirectory("micronaut-guides").toFile();

        List<Guide> metadatas = new ArrayList<>();

        String pathBase = "src/test/resources/guides/hello-base";
        File fileBase = new File(pathBase);
        guideParser.parseGuideMetadata(fileBase, "metadata.json").ifPresent(metadatas::add);
        String path = "src/test/resources/guides/creating-your-first-micronaut-app";
        File file = new File(path);
        guideParser.parseGuideMetadata(file, "metadata.json").ifPresent(metadatas::add);

        guideProjectGenerator.generate(outputDirectory, metadatas.get(0));
        guideProjectGenerator.generate(outputDirectory, metadatas.get(1));

        filesTransferUtility.transferFiles(fileBase, outputDirectory, metadatas.get(0), metadatas);
        filesTransferUtility.transferFiles(file, outputDirectory, metadatas.get(1), metadatas);

        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-kotlin").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-kotlin").exists());

        assertFalse(new File(outputDirectory, "/hello-base-gradle-groovy").exists());
        assertFalse(new File(outputDirectory, "/hello-base-gradle-java").exists());
        assertFalse(new File(outputDirectory, "/hello-base-gradle-kotlin").exists());
        assertFalse(new File(outputDirectory, "/hello-base-maven-groovy").exists());
        assertFalse(new File(outputDirectory, "/hello-base-maven-java").exists());
        assertFalse(new File(outputDirectory, "/hello-base-maven-kotlin").exists());

        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-groovy/src/main/groovy/example/micronaut/HelloController.groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-java/src/main/java/example/micronaut/HelloController.java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-kotlin/src/main/kotlin/example/micronaut/HelloController.kt").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-groovy/src/main/groovy/example/micronaut/HelloController.groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-java/src/main/java/example/micronaut/HelloController.java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-kotlin/src/main/kotlin/example/micronaut/HelloController.kt").exists());

        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-groovy/src/main/groovy/example/micronaut/Application.groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-java/src/main/java/example/micronaut/Application.java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-gradle-kotlin/src/main/kotlin/example/micronaut/Application.kt").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-groovy/src/main/groovy/example/micronaut/Application.groovy").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-java/src/main/java/example/micronaut/Application.java").exists());
        assertTrue(new File(outputDirectory, "/creating-your-first-micronaut-app-maven-kotlin/src/main/kotlin/example/micronaut/Application.kt").exists());
    }

}

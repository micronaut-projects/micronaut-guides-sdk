/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.core;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import io.micronaut.guides.core.html.CategoriesIndexGenerator;
import io.micronaut.guides.core.html.GuideMatrixGenerator;
import io.micronaut.guides.core.html.GuidePageGenerator;
import io.micronaut.guides.core.html.IndexGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Predicate;

/**
 * Default implementation of the {@link WebsiteGenerator} interface.
 * This class is responsible for generating a website from the specified input directory to the specified output directory.
 */
@Internal
@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWebsiteGenerator.class);

    private final GuideParser guideParser;
    private final GuideProjectGenerator guideProjectGenerator;
    private final JsonFeedGenerator jsonFeedGenerator;
    private final RssFeedGenerator rssFeedGenerator;
    private final FilesTransferUtility filesTransferUtility;
    private final TestScriptGenerator testScriptGenerator;
    private final IndexGenerator indexGenerator;
    private final GuideMatrixGenerator guideMatrixGenerator;
    private final GuideProjectZipper guideProjectZipper;
    private final GuidesConfiguration guidesConfiguration;
    private final GuidePageGenerator guidePageGenerator;
    private final CategoriesIndexGenerator categoriesIndexGenerator;
    private final AsciidocConfiguration asciidocConfiguration;

    @SuppressWarnings("checkstyle:ParameterNumber")
    protected DefaultWebsiteGenerator(GuideParser guideParser,
                                      GuideProjectGenerator guideProjectGenerator,
                                      JsonFeedGenerator jsonFeedGenerator,
                                      RssFeedGenerator rssFeedGenerator,
                                      FilesTransferUtility filesTransferUtility,
                                      TestScriptGenerator testScriptGenerator,
                                      IndexGenerator indexGenerator,
                                      GuideMatrixGenerator guideMatrixGenerator,
                                      GuideProjectZipper guideProjectZipper,
                                      GuidePageGenerator guidePageGenerator,
                                      AsciidocConfiguration asciidocConfiguration,
                                      GuidesConfiguration guidesConfiguration,
                                      CategoriesIndexGenerator categoriesIndexGenerator) {
        this.guideParser = guideParser;
        this.guideProjectGenerator = guideProjectGenerator;
        this.jsonFeedGenerator = jsonFeedGenerator;
        this.rssFeedGenerator = rssFeedGenerator;
        this.filesTransferUtility = filesTransferUtility;
        this.testScriptGenerator = testScriptGenerator;
        this.indexGenerator = indexGenerator;
        this.guideMatrixGenerator = guideMatrixGenerator;
        this.guideProjectZipper = guideProjectZipper;
        this.guidesConfiguration = guidesConfiguration;
        this.guidePageGenerator = guidePageGenerator;
        this.categoriesIndexGenerator = categoriesIndexGenerator;
        this.asciidocConfiguration = asciidocConfiguration;
    }

    @Override
    public void generate(@NonNull @NotNull File inputDirectory, @NonNull @NotNull File outputDirectory) throws IOException {
        generate(inputDirectory, outputDirectory, null);
    }

    @Override
    public void generate(
            @NonNull @NotNull File inputDirectory,
            @NonNull @NotNull File outputDirectory,
            @Nullable Predicate<Guide> condition) throws IOException {

        File guidesInputDirectory = new File(inputDirectory, guidesConfiguration.getGuidesDir());
        if (!guidesInputDirectory.exists()) {
            throw new ConfigurationException("Guides directory " + guidesInputDirectory.getAbsolutePath() + " not found");
        }
        if (!guidesInputDirectory.isDirectory()) {
            throw new ConfigurationException("Guides path " + guidesInputDirectory.getAbsolutePath() + " is not a directory");
        }

        List<? extends Guide> guides = guideParser.parseGuidesMetadata(guidesInputDirectory);

        if (condition != null) {
            List<String> bases = guides.stream().filter(condition).map(Guide::getBase).toList();
            guides = guides.stream().filter(guide -> bases.contains(guide.getSlug()) || condition.test(guide)).toList();
        }

        for (Guide guide : guides) {
            File guideInputDirectory = guide.getFolder();
            File guideOutput = new File(outputDirectory, guide.getSlug());

            guideOutput.mkdir();
            guideProjectGenerator.generate(guideOutput, guide);
            filesTransferUtility.transferFiles(guideInputDirectory, guideOutput, guide, guides);

            // Test script generation
            testScriptGenerator.generateTestScript(outputDirectory, guide);

            // Native Test script generation
            testScriptGenerator.generateNativeTestScript(outputDirectory, guide);

            guideProjectZipper.zipGuide(guide, guideOutput, outputDirectory);

            guidePageGenerator.generatePage(guide, inputDirectory, outputDirectory, guideOutput);

            guideMatrixGenerator.renderIndex(guide, outputDirectory);
        }

        guides = guides.stream().filter(Guide::isPublish).toList();

        indexGenerator.renderIndex(guides, outputDirectory);

        categoriesIndexGenerator.renderIndex(guides, outputDirectory);

        rssFeedGenerator.rssFeed(guides, outputDirectory);

        jsonFeedGenerator.jsonFeedString(guides, outputDirectory);

        File imagesFolder = new File(inputDirectory, asciidocConfiguration.getImagesdir());
        if (imagesFolder.exists()) {
            File outputImagesFolder = new File(outputDirectory, asciidocConfiguration.getImagesdir());
            if (!outputImagesFolder.exists()) {
                outputImagesFolder.mkdir();
            }

            copyFolder(imagesFolder.toPath(), outputImagesFolder.toPath());
        }
    }

    protected static void copyFolder(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = destination.resolve(source.relativize(dir));
                if (!Files.exists(targetPath)) {
                    Files.createDirectory(targetPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

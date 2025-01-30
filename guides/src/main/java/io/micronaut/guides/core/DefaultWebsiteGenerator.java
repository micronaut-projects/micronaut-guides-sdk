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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import io.micronaut.guides.core.html.GuidePageGenerator;
import io.micronaut.guides.core.html.categories.CategoriesIndexFileGenerator;
import io.micronaut.guides.core.html.index.IndexFileGenerator;
import io.micronaut.guides.core.html.matrix.GuideMatrixFileGenerator;
import io.micronaut.guides.core.jsonfeed.JsonFeedFileGenerator;
import io.micronaut.guides.core.rss.RssFeedFileGenerator;
import io.micronaut.guides.core.test.TestScriptFileGenerator;
import io.micronaut.guides.core.zip.GuideProjectZipper;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;

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
    private final GuideParser guideParser;
    private final GuideProjectGenerator guideProjectGenerator;
    private final JsonFeedFileGenerator jsonFeedFileGenerator;
    private final RssFeedFileGenerator rssFeedFileGenerator;
    private final FilesTransferUtility filesTransferUtility;
    private final TestScriptFileGenerator testScriptFileGenerator;
    private final IndexFileGenerator indexFileGenerator;
    private final GuideMatrixFileGenerator guideMatrixFileGenerator;
    private final GuideProjectZipper guideProjectZipper;
    private final GuidesConfiguration guidesConfiguration;
    private final GuidePageGenerator guidePageGenerator;
    private final CategoriesIndexFileGenerator categoriesIndexFileGenerator;
    private final AsciidocConfiguration asciidocConfiguration;

    @SuppressWarnings("checkstyle:ParameterNumber")
    public DefaultWebsiteGenerator(GuideParser guideParser,
                                   GuideProjectGenerator guideProjectGenerator,
                                   JsonFeedFileGenerator jsonFeedFileGenerator,
                                   RssFeedFileGenerator rssFeedFileGenerator,
                                   FilesTransferUtility filesTransferUtility,
                                   TestScriptFileGenerator testScriptFileGenerator,
                                   IndexFileGenerator indexFileGenerator,
                                   GuideMatrixFileGenerator guideMatrixFileGenerator,
                                   GuideProjectZipper guideProjectZipper,
                                   GuidePageGenerator guidePageGenerator,
                                   AsciidocConfiguration asciidocConfiguration,
                                   GuidesConfiguration guidesConfiguration,
                                   CategoriesIndexFileGenerator categoriesIndexFileGenerator) {
        this.guideParser = guideParser;
        this.guideProjectGenerator = guideProjectGenerator;
        this.jsonFeedFileGenerator = jsonFeedFileGenerator;
        this.rssFeedFileGenerator = rssFeedFileGenerator;
        this.filesTransferUtility = filesTransferUtility;
        this.testScriptFileGenerator = testScriptFileGenerator;
        this.indexFileGenerator = indexFileGenerator;
        this.guideMatrixFileGenerator = guideMatrixFileGenerator;
        this.guideProjectZipper = guideProjectZipper;
        this.guidesConfiguration = guidesConfiguration;
        this.guidePageGenerator = guidePageGenerator;
        this.categoriesIndexFileGenerator = categoriesIndexFileGenerator;
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
        guides = filter(guides, condition);
        generate(inputDirectory, guides, outputDirectory);
    }

    /**
     * @param guides    Guides
     * @param condition Condition to filter the guides against
     * @return List of filtered guides
     */
    protected List<? extends Guide> filter(List<? extends Guide> guides, @Nullable Predicate<Guide> condition) {
        if (condition == null) {
            return guides;
        }
        List<String> bases = guides.stream().filter(condition).map(Guide::getBase).toList();
        return guides.stream().filter(guide -> bases.contains(guide.getSlug()) || condition.test(guide)).toList();
    }

    /**
     * @param outputDirectory Output directory
     * @param guide           Guide
     * @return The folder where the guides projects will be generated into.
     */
    protected File guideOutput(File outputDirectory, Guide guide) {
        return new File(outputDirectory, guide.getSlug());
    }

    /**
     * @param inputDirectory  Input directory
     * @param guides          Guides
     * @param outputDirectory Output directory
     * @throws IOException If an I/O error occurs reading from the file.
     */
    protected void generate(
            @NonNull File inputDirectory,
            @NonNull List<? extends Guide> guides,
            @NonNull File outputDirectory) throws IOException {
        for (Guide guide : guides) {
            generate(inputDirectory, guides, outputDirectory, guide);
        }
        generateIndexesAndFeeds(guides, outputDirectory);
        copyImages(inputDirectory, outputDirectory);
    }

    /**
     * @param allGuides       All Guides
     * @param outputDirectory Output Directory
     * @throws IOException If an I/O error occurs while generating the indexes and feeds
     */
    protected void generateIndexesAndFeeds(List<? extends Guide> allGuides, File outputDirectory) throws IOException {
        List<? extends Guide> guides = allGuides.stream().filter(Guide::isPublish).toList();
        if (CollectionUtils.isNotEmpty(guides)) {
            indexFileGenerator.renderIndex(guides, outputDirectory);
            categoriesIndexFileGenerator.saveCategoryIndex(guides, outputDirectory);
            rssFeedFileGenerator.saveRssFeed(guides, outputDirectory);
            jsonFeedFileGenerator.saveJsonFeed(guides, outputDirectory);
        }
    }

    /**
     * @param inputDirectory  Input directory
     * @param outputDirectory Output directory
     * @throws IOException If an I/O error occurs while copying the folder
     */
    protected void copyImages(File inputDirectory, File outputDirectory) throws IOException {
        File imagesFolder = new File(inputDirectory, asciidocConfiguration.getImagesdir());
        if (imagesFolder.exists()) {
            File outputImagesFolder = new File(outputDirectory, asciidocConfiguration.getImagesdir());
            if (!outputImagesFolder.exists()) {
                outputImagesFolder.mkdir();
            }
            copyFolder(imagesFolder.toPath(), outputImagesFolder.toPath());
        }
    }

    /**
     * Generate a Guide.
     *
     * @param inputDirectory  Input Directroy
     * @param guides          Guides
     * @param outputDirectory Output Directory
     * @param guide           Guide Being Generated
     * @throws IOException If an I/O error occurs reading from the file.
     */
    protected void generate(File inputDirectory, List<? extends Guide> guides, File outputDirectory, Guide guide) throws IOException {
        boolean publish = guide.isPublish();
        File guideInputDirectory = guide.getFolder();
        File guideOutput = guideOutput(outputDirectory, guide);
        guideOutput.mkdir();
        if (CollectionUtils.isNotEmpty(guide.getApps())) {
            guideProjectGenerator.generate(guideOutput, guide);
            filesTransferUtility.transferFiles(guideInputDirectory, guideOutput, guide, guides);
            testScriptFileGenerator.saveTestScript(outputDirectory, guide);
            testScriptFileGenerator.saveNativeTestScript(outputDirectory, guide);
            if (publish) {
                guideProjectZipper.zipGuide(guide, guideOutput, outputDirectory);
                guideMatrixFileGenerator.saveMatrix(guide, outputDirectory);
            }
        }
        if (publish) {
            guidePageGenerator.generatePage(guide, guides, inputDirectory, outputDirectory, guideOutput);
        }
    }

    private static void copyFolder(Path source, Path destination) throws IOException {
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

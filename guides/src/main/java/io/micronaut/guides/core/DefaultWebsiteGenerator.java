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
import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import io.micronaut.guides.core.asciidoc.AsciidocConverter;
import io.micronaut.guides.core.asciidoc.GuideRenderAttributesProvider;
import io.micronaut.guides.core.html.CategoriesIndexGenerator;
import io.micronaut.guides.core.html.GuideMatrixGenerator;
import io.micronaut.guides.core.html.GuidePageGenerator;
import io.micronaut.guides.core.html.IndexGenerator;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link WebsiteGenerator} interface.
 * This class is responsible for generating a website from the specified input directory to the specified output directory.
 */
@Internal
@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWebsiteGenerator.class);
    private static final String FILENAME_TEST_SH = "test.sh";
    private static final String FILENAME_NATIVE_TEST_SH = "native-test.sh";
    private static final String FILENAME_INDEX_HTML = "index.html";
    private static final String FILENAME_CATEGORIES_INDEX_HTML = "categories-index.html";
    private final GuideRenderAttributesProvider guideRenderAttributesProvider;
    private final GuideParser guideParser;
    private final GuideProjectGenerator guideProjectGenerator;
    private final JsonFeedGenerator jsonFeedGenerator;
    private final RssFeedGenerator rssFeedGenerator;
    private final FilesTransferUtility filesTransferUtility;
    private final TestScriptGenerator testScriptGenerator;
    private final MacroSubstitution macroSubstitution;
    private final AsciidocConverter asciidocConverter;
    private final IndexGenerator indexGenerator;
    private final GuideMatrixGenerator guideMatrixGenerator;
    private final GuideProjectZipper guideProjectZipper;
    private final RssFeedConfiguration rssFeedConfiguration;
    private final JsonFeedConfiguration jsonFeedConfiguration;
    private final GuidesConfiguration guidesConfiguration;
    private final GuidePageGenerator guidePageGenerator;
    private final CategoriesIndexGenerator categoriesIndexGenerator;
    private final AsciidocConfiguration asciidocConfiguration;

    @SuppressWarnings("checkstyle:ParameterNumber")
    public DefaultWebsiteGenerator(GuideRenderAttributesProvider guideRenderAttributesProvider, GuideParser guideParser,
                            GuideProjectGenerator guideProjectGenerator,
                            JsonFeedGenerator jsonFeedGenerator,
                            RssFeedGenerator rssFeedGenerator,
                            FilesTransferUtility filesTransferUtility,
                            TestScriptGenerator testScriptGenerator,
                            MacroSubstitution macroSubstitution,
                            AsciidocConverter asciidocConverter,
                            IndexGenerator indexGenerator,
                            GuideMatrixGenerator guideMatrixGenerator,
                            GuideProjectZipper guideProjectZipper,
                            RssFeedConfiguration rssFeedConfiguration,
                            JsonFeedConfiguration jsonFeedConfiguration,
                            GuidePageGenerator guidePageGenerator,
                            AsciidocConfiguration asciidocConfiguration,
                            GuidesConfiguration guidesConfiguration,
                            CategoriesIndexGenerator categoriesIndexGenerator) {
        this.guideRenderAttributesProvider = guideRenderAttributesProvider;
        this.guideParser = guideParser;
        this.guideProjectGenerator = guideProjectGenerator;
        this.jsonFeedGenerator = jsonFeedGenerator;
        this.rssFeedGenerator = rssFeedGenerator;
        this.filesTransferUtility = filesTransferUtility;
        this.testScriptGenerator = testScriptGenerator;
        this.macroSubstitution = macroSubstitution;
        this.asciidocConverter = asciidocConverter;
        this.indexGenerator = indexGenerator;
        this.guideMatrixGenerator = guideMatrixGenerator;
        this.guideProjectZipper = guideProjectZipper;
        this.rssFeedConfiguration = rssFeedConfiguration;
        this.jsonFeedConfiguration = jsonFeedConfiguration;
        this.guidesConfiguration = guidesConfiguration;
        this.guidePageGenerator = guidePageGenerator;
        this.categoriesIndexGenerator = categoriesIndexGenerator;
        this.asciidocConfiguration = asciidocConfiguration;
    }

    @Override
    public void generate(@NonNull @NotNull File inputDirectory, @NonNull @NotNull File outputDirectory) throws IOException {
        File guidesInputDirectory = new File(inputDirectory, guidesConfiguration.getGuidesDir());
        if (!guidesInputDirectory.exists()) {
            throw new ConfigurationException("Guides directory " + guidesInputDirectory.getAbsolutePath() + " not found");
        }
        if (!guidesInputDirectory.isDirectory()) {
            throw new ConfigurationException("Guides path " + guidesInputDirectory.getAbsolutePath() + " is not a directory");
        }
        List<? extends Guide> guides = guideParser.parseGuidesMetadata(guidesInputDirectory);
        for (Guide guide : guides) {
            if (guide.isPublish()) {
                File guideInputDirectory = guide.getFolder();
                File asciidocFile = new File(guideInputDirectory, guide.getAsciidoctor());
                if (!asciidocFile.exists()) {
                    throw new ConfigurationException("asciidoc file not found for " + guide.getSlug());
                }

                String asciidoc = readFile(asciidocFile);

                if (guide.getApps().isEmpty()) {
                    renderHtml(asciidoc, new GuideRender(guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)), inputDirectory, outputDirectory, guide.getSlug(), guideInputDirectory);
                } else {
                    File guideOutput = new File(outputDirectory, guide.getSlug());
                    guideOutput.mkdir();
                    guideProjectGenerator.generate(guideOutput, guide);
                    filesTransferUtility.transferFiles(guideInputDirectory, guideOutput, guide, guides);

                    // Test script generation
                    String testScript = testScriptGenerator.generateTestScript(outputDirectory, new ArrayList<>(List.of(guide)));
                    saveToFile(testScript, guideOutput, FILENAME_TEST_SH, true);

                    // Native Test script generation
                    String nativeTestScript = testScriptGenerator.generateNativeTestScript(outputDirectory, new ArrayList<>(List.of(guide)));
                    saveToFile(nativeTestScript, guideOutput, FILENAME_NATIVE_TEST_SH, true);

                    List<GuidesOption> guideOptions = GuideGenerationUtils.guidesOptions(guide, LOG);
                    for (GuidesOption guidesOption : guideOptions) {
                        String name = MacroUtils.getSourceDir(guide.getSlug(), guidesOption);
                        zipGuide(outputDirectory, guideOutput, name);
                        renderHtml(asciidoc, new GuideRender(guide, guidesOption), inputDirectory, outputDirectory, name, guideOutput);
                    }

                    String guideMatrixHtml = guideMatrixGenerator.renderIndex(guide);
                    saveToFile(guideMatrixHtml, outputDirectory, guide.getSlug() + ".html");
                }
            }
        }

        guides = guides.stream().filter(Guide::isPublish).toList();

        String indexHtml = indexGenerator.renderIndex(guides);
        saveToFile(indexHtml, outputDirectory, FILENAME_INDEX_HTML);

        String moduleIndexHtml = categoriesIndexGenerator.renderIndex(guides);
        saveToFile(moduleIndexHtml, outputDirectory, FILENAME_CATEGORIES_INDEX_HTML);

        String rss = rssFeedGenerator.rssFeed(guides);
        saveToFile(rss, outputDirectory, rssFeedConfiguration.getFilename());

        String json = jsonFeedGenerator.jsonFeedString(guides);
        saveToFile(json, outputDirectory, jsonFeedConfiguration.getFilename());

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
     *
     * @param outputDirectory Output Directory
     * @param guideOutput Guide Output
     * @param name Guide Option name
     * @throws IOException if an I/O error occurs during zipping
     */
    protected void zipGuide(File outputDirectory, File guideOutput, String name) throws IOException {
        File zipFile = new File(outputDirectory, name + ".zip");
        File folderFile = new File(guideOutput, name);
        guideProjectZipper.zipDirectory(folderFile.getAbsolutePath(), zipFile.getAbsolutePath());
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

    private void renderHtml(String asciidoc, GuideRender guideRender, File inputDirectory, File outputDirectory, String name, File guideOutput) throws IOException {
        // Macro substitution
        String optionAsciidoc = macroSubstitution.substitute(asciidoc, guideRender);

        // HTML rendering
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sourcedir", outputDirectory.getAbsolutePath());
        attributes.put("guidesourcedir", new File(guideOutput, name).getAbsolutePath());
        attributes.putAll(guideRenderAttributesProvider.attributes(guideRender));
        String guideOptionHtmlFileName = name + ".html";
        String optionHtml = asciidocConverter.convert(optionAsciidoc, inputDirectory, () -> attributes);
        if (!asciidocConfiguration.isHeaderFooter()) {
            List<String> extractedToc = extractToc(optionHtml);
            String tocHtml;

            if (extractedToc.isEmpty()) {
                tocHtml = "";
            } else {
                tocHtml = extractedToc.get(0);
                for (String toc : extractedToc) {
                    optionHtml = optionHtml.replace(toc + "\n", "");
                }
            }

            Guide guide = guideRender.guide();
            optionHtml = guidePageGenerator.render(tocHtml, optionHtml);
            optionHtml = optionHtml.replace("{title}", guide.getTitle());
            if (guide.getCategories().isEmpty()) {
                optionHtml = optionHtml.replace("{section}", "");
                optionHtml = optionHtml.replace("{section-link}", "");

            } else {
                optionHtml = optionHtml.replace("{section}", guide.getCategories().get(0));
                optionHtml = optionHtml.replace("{section-link}", "https://graal.cloud/gdk/docs/gdk-modules/" + guide.getCategories().get(0).toLowerCase() + "/");
            }
        }
        saveToFile(optionHtml, outputDirectory, guideOptionHtmlFileName);
    }

    private List<String> extractToc(String html) {
        List<String> tocDivs = new ArrayList<>();
        String openDivPattern = "<div";
        String closeDivPattern = "</div>";
        String classAttribute = "class=\"toc-floating\"";
        String idAttribute = "id=\"toc\"";

        int startIndex = html.indexOf(openDivPattern + " " + classAttribute);
        if (startIndex != -1) {
            int openingTagEnd = html.indexOf(">", startIndex);
            if (openingTagEnd != -1) {
                int nestedDivCount = 0;
                int currentIndex = openingTagEnd + 1;

                while (currentIndex < html.length()) {
                    int nextOpenDiv = html.indexOf(openDivPattern, currentIndex);
                    int nextCloseDiv = html.indexOf(closeDivPattern, currentIndex);

                    if (nextCloseDiv == -1) {
                        break;
                    }

                    if (nextOpenDiv != -1 && nextOpenDiv < nextCloseDiv) {
                        nestedDivCount++;
                        currentIndex = nextOpenDiv + openDivPattern.length();
                    } else {
                        if (nestedDivCount == 0) {
                            tocDivs.add(html.substring(startIndex, nextCloseDiv + closeDivPattern.length()));
                            break;
                        }
                        nestedDivCount--;
                        currentIndex = nextCloseDiv + closeDivPattern.length();
                    }
                }
            }
        }

        startIndex = html.indexOf(openDivPattern + " " + idAttribute);
        if (startIndex == -1) {
            startIndex = html.indexOf(openDivPattern + " id='toc'");
        }

        if (startIndex != -1) {
            int openingTagEnd = html.indexOf(">", startIndex);
            if (openingTagEnd != -1) {
                int nestedDivCount = 0;
                int currentIndex = openingTagEnd + 1;

                while (currentIndex < html.length()) {
                    int nextOpenDiv = html.indexOf(openDivPattern, currentIndex);
                    int nextCloseDiv = html.indexOf(closeDivPattern, currentIndex);

                    if (nextCloseDiv == -1) {
                        break;
                    }

                    if (nextOpenDiv != -1 && nextOpenDiv < nextCloseDiv) {
                        nestedDivCount++;
                        currentIndex = nextOpenDiv + openDivPattern.length();
                    } else {
                        if (nestedDivCount == 0) {
                            tocDivs.add(html.substring(startIndex, nextCloseDiv + closeDivPattern.length()));
                            break;
                        }
                        nestedDivCount--;
                        currentIndex = nextCloseDiv + closeDivPattern.length();
                    }
                }
            }
        }

        return tocDivs;
    }

    private void saveToFile(String content, File outputDirectory, String filename) throws IOException {
        saveToFile(content, outputDirectory, filename, false);
    }

    private void saveToFile(String content, File outputDirectory, String filename, boolean executable) throws IOException {
        Path filePath = Paths.get(outputDirectory.getAbsolutePath(), filename);
        Files.write(filePath, content.getBytes());
        if (executable) {
            filePath.toFile().setExecutable(true);
        }
    }

    private static String readFile(File file) throws IOException {
        Path path = file.toPath();
        return new String(Files.readAllBytes(path));
    }
}

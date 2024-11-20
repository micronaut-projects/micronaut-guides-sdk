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
import io.micronaut.guides.core.asciidoc.AsciidocConverter;
import io.micronaut.guides.core.html.GuideMatrixGenerator;
import io.micronaut.guides.core.html.IndexGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Internal
@Singleton
class DefaultWebsiteGenerator implements WebsiteGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWebsiteGenerator.class);
    private static final String FILENAME_TEST_SH = "test.sh";
    private static final String FILENAME_NATIVE_TEST_SH = "native-test.sh";
    private static final String FILENAME_INDEX_HTML = "index.html";
    private final GuideParser guideParser;
    private final GuideProjectGenerator guideProjectGenerator;
    private final JsonFeedGenerator jsonFeedGenerator;
    private final JsonFeedConfiguration jsonFeedConfiguration;
    private final RssFeedGenerator rssFeedGenerator;
    private final RssFeedConfiguration rssFeedConfiguration;
    private final FilesTransferUtility filesTransferUtility;
    private final TestScriptGenerator testScriptGenerator;
    private final MacroSubstitution macroSubstitution;
    private final AsciidocConverter asciidocConverter;
    private final IndexGenerator indexGenerator;
    private final GuideMatrixGenerator guideMatrixGenerator;

    DefaultWebsiteGenerator(GuideParser guideParser,
                            GuideProjectGenerator guideProjectGenerator,
                            JsonFeedGenerator jsonFeedGenerator,
                            JsonFeedConfiguration jsonFeedConfiguration,
                            RssFeedGenerator rssFeedGenerator,
                            RssFeedConfiguration rssFeedConfiguration,
                            FilesTransferUtility filesTransferUtility,
                            TestScriptGenerator testScriptGenerator, MacroSubstitution macroSubstitution, AsciidocConverter asciidocConverter, IndexGenerator indexGenerator, GuideMatrixGenerator guideMatrixGenerator) {
        this.guideParser = guideParser;
        this.guideProjectGenerator = guideProjectGenerator;
        this.jsonFeedGenerator = jsonFeedGenerator;
        this.jsonFeedConfiguration = jsonFeedConfiguration;
        this.rssFeedGenerator = rssFeedGenerator;
        this.rssFeedConfiguration = rssFeedConfiguration;
        this.filesTransferUtility = filesTransferUtility;
        this.testScriptGenerator = testScriptGenerator;
        this.macroSubstitution = macroSubstitution;
        this.asciidocConverter = asciidocConverter;
        this.indexGenerator = indexGenerator;
        this.guideMatrixGenerator = guideMatrixGenerator;
    }

    @Override
    public void generate(@NonNull @NotNull File inputDirectory,
                         @NonNull @NotNull File outputDirectory) throws IOException {
        List<Guide> guides = guideParser.parseGuidesMetadata(inputDirectory);
        for (Guide guide : guides) {
            File guideOutput = new File(outputDirectory, guide.slug());
            guideOutput.mkdir();
            guideProjectGenerator.generate(guideOutput, guide);
            File guideInputDirectory = new File(inputDirectory, guide.slug());
            filesTransferUtility.transferFiles(guideInputDirectory, guideOutput, guide);

            // Test script generation
            String testScript = testScriptGenerator.generateTestScript(new ArrayList<>(List.of(guide)));
            saveToFile(testScript, guideOutput, FILENAME_TEST_SH);

            // Native Test script generation
            String nativeTestScript = testScriptGenerator.generateNativeTestScript(new ArrayList<>(List.of(guide)));
            saveToFile(nativeTestScript, guideOutput, FILENAME_NATIVE_TEST_SH);

            // HTML rendering
            File asciidocFile = new File(guideInputDirectory, guide.slug() + ".adoc");
            if (!asciidocFile.exists()) {
                throw new ConfigurationException("asciidoc file not found for " + guide.slug());
            }
            List<GuidesOption> guideOptions = GuideGenerationUtils.guidesOptions(guide, LOG);
            String asciidoc = readFile(asciidocFile);
            for (GuidesOption guidesOption : guideOptions) {
                String optionAsciidoc = macroSubstitution.substitute(asciidoc, guide, guidesOption);
                File guideOptionOutput = new File(guideOutput, guide.slug() + "-" + guidesOption.getBuildTool() + "-" + guidesOption.getLanguage());
                String optionHtml = asciidocConverter.convert(optionAsciidoc, guideOutput.getAbsolutePath());
                String guideOptionHtmlFileName = guide.slug() + "-" + guidesOption.getBuildTool() + "-" + guidesOption.getLanguage() + ".html";
                saveToFile(optionHtml, outputDirectory, guideOptionHtmlFileName);
            }

            String guideMatrixHtml = guideMatrixGenerator.renderIndex(guide);
            saveToFile(guideMatrixHtml, outputDirectory, guide.slug() + ".html");
        }

        String indexHtml = indexGenerator.renderIndex(guides);
        saveToFile(indexHtml, outputDirectory, FILENAME_INDEX_HTML);

        String rss = rssFeedGenerator.rssFeed(guides);
        saveToFile(rss, outputDirectory, rssFeedConfiguration.getFilename());

        String json = jsonFeedGenerator.jsonFeedString(guides);
        saveToFile(json, outputDirectory, jsonFeedConfiguration.getFilename());
    }

    private void saveToFile(String content,
                           File outputDirectory,
                           String filename) throws IOException {
        Path filePath = Paths.get(outputDirectory.getAbsolutePath(), filename);
        Files.write(filePath, content.getBytes());
    }

    private static String readFile(File file) throws IOException {
        Path path = file.toPath();
        return new String(Files.readAllBytes(path));
    }
}

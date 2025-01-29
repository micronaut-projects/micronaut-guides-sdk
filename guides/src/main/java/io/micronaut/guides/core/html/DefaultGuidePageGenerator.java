/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.guides.core.html;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.guides.core.*;
import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import io.micronaut.guides.core.asciidoc.AsciidocConverter;
import io.micronaut.guides.core.asciidoc.GuideRenderAttributesProvider;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link GuidePageGenerator} interface.
 * This class is responsible for generating the HTML pages for guides.
 */
@Singleton
public class DefaultGuidePageGenerator implements GuidePageGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuidePageGenerator.class);
    private final GuidesConfiguration guidesConfiguration;
    private final GuideRenderAttributesProvider guideRenderAttributesProvider;
    private final MacroSubstitution macroSubstitution;
    private final AsciidocConverter asciidocConverter;
    private final AsciidocConfiguration asciidocConfiguration;

    protected DefaultGuidePageGenerator(GuidesConfiguration guidesConfiguration,
                                        MacroSubstitution macroSubstitution,
                                        AsciidocConverter asciidocConverter,
                                        AsciidocConfiguration asciidocConfiguration,
                                        GuideRenderAttributesProvider guideRenderAttributesProvider) {
        this.guidesConfiguration = guidesConfiguration;
        this.guideRenderAttributesProvider = guideRenderAttributesProvider;
        this.macroSubstitution = macroSubstitution;
        this.asciidocConverter = asciidocConverter;
        this.asciidocConfiguration = asciidocConfiguration;
    }

    @Override
    public void generatePage(Guide guide, File inputDirectory, File outputDirectory) throws IOException {
        if (guide.getApps().isEmpty()) {
            renderHtml(guide, null, inputDirectory, outputDirectory, guide.getSlug());
        } else {
            List<GuidesOption> guideOptions = GuideGenerationUtils.guidesOptions(guide, LOG);
            for (GuidesOption guidesOption : guideOptions) {
                String name = MacroUtils.getSourceDir(guide.getSlug(), guidesOption);
                renderHtml(guide, guidesOption, inputDirectory, outputDirectory, name);
            }
        }
    }

    /**
     * Renders the HTML for the given guide.
     *
     * @param guide           The guide
     * @param option          The option
     * @param inputDirectory  The input directory
     * @param outputDirectory The output directory
     * @param fileName        The file name
     * @throws IOException If an error occurs
     */
    protected void renderHtml(Guide guide, GuidesOption option, File inputDirectory, File outputDirectory, String fileName) throws IOException {
        GuideRender guideRender = new GuideRender(guide, option);

        File guideInputDirectory = guide.getFolder();
        File asciidocFile = new File(guideInputDirectory, guide.getAsciidoctor());

        String asciidoc = readFile(asciidocFile);


        if (!asciidocFile.exists()) {
            throw new ConfigurationException("asciidoc file not found for " + guide.getSlug());
        }

        // Macro substitution
        String optionAsciidoc = macroSubstitution.substitute(asciidoc, guideRender);

        // HTML rendering
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sourcedir", outputDirectory.getAbsolutePath());
        if (option != null) {
            attributes.put("guidesourcedir", guide.getOutputDirectory(outputDirectory, option).getAbsolutePath());
        }
        attributes.putAll(guideRenderAttributesProvider.attributes(guideRender));
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

            optionHtml = applyTemplate(tocHtml, optionHtml);
            optionHtml = optionHtml.replace("{title}", guide.getTitle());
            if (guide.getCategories().isEmpty()) {
                optionHtml = optionHtml.replace("{section}", "");
                optionHtml = optionHtml.replace("{section-link}", "");

            } else {
                optionHtml = optionHtml.replace("{section}", guide.getCategories().get(0));
                optionHtml = optionHtml.replace("{section-link}", "https://graal.cloud/gdk/docs/gdk-modules/" + guide.getCategories().get(0).toLowerCase() + "/");
            }
        }

        saveFile(optionHtml, outputDirectory, fileName + ".html");

    }

    /**
     * Applies the HTML template to the given table of contents (TOC) and HTML content.
     *
     * @param toc  The table of contents as a string.
     * @param html The HTML content as a string.
     * @return The combined HTML content with the TOC applied.
     */
    protected String applyTemplate(String toc, String html) {
        return HtmlUtils.html5(guidesConfiguration.getTitle(), toc + html);
    }

    /**
     * Extracts the table of contents (TOC) from the given HTML content.
     *
     * @param html The HTML content as a string.
     * @return A list of strings representing the TOC div elements found in the HTML.
     */
    protected List<String> extractToc(String html) {
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

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param file The file to read.
     * @return The content of the file as a string.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    protected static String readFile(File file) throws IOException {
        Path path = file.toPath();
        return new String(Files.readAllBytes(path));
    }
}

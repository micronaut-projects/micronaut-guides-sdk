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
package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.Guide;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.asciidoctor.*;
import org.asciidoctor.extension.*;
import org.slf4j.MDC;

import java.io.File;
import java.util.List;

/**
 * DefaultAsciidocConverter is a singleton class that implements the AsciidocConverter interface.
 * It provides methods to convert Asciidoc files to html using Asciidoctor.
 */
@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    public static final String ATTRIBUTE_GUIDE = "guide";
    OptionsBuilder optionsBuilder;
    AttributesBuilder attributesBuilder;

    Asciidoctor asciidoctor;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration,
                             List<IncludeProcessor> includeProcessors,
                             List<BlockProcessor> blockProcessors,
                             List<BlockMacroProcessor> blockMacroProcessors,
                             List<InlineMacroProcessor> inLineMacroProcessors,
                             List<Preprocessor> preProcessors) {
        attributesBuilder = Attributes.builder()
                .sourceHighlighter(asciidocConfiguration.getSourceHighlighter())
                .tableOfContents(asciidocConfiguration.getToc())
                .attribute("toclevels", asciidocConfiguration.getToclevels())
                .attribute("toc-title", "")
                .sectionNumbers(asciidocConfiguration.getSectnums())
                .attribute("idprefix", asciidocConfiguration.getIdprefix())
                .attribute("idseparator", asciidocConfiguration.getIdseparator())
                .icons(asciidocConfiguration.getIcons())
                .imagesDir(asciidocConfiguration.getImagesdir())
                .noFooter(asciidocConfiguration.isNofooter());

        optionsBuilder = Options.builder()
                .eruby(asciidocConfiguration.getRuby())
                .docType("book")
                .safe(SafeMode.UNSAFE);

        if (StringUtils.isNotEmpty(asciidocConfiguration.getBaseDir())) {
            optionsBuilder.baseDir(new File(asciidocConfiguration.getBaseDir()));
        }

        asciidoctor = Asciidoctor.Factory.create();
        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();
        includeProcessors.forEach(javaExtensionRegistry::includeProcessor);
        blockProcessors.forEach(javaExtensionRegistry::block);
        blockMacroProcessors.forEach(javaExtensionRegistry::blockMacro);
        inLineMacroProcessors.forEach(javaExtensionRegistry::inlineMacro);
        preProcessors.forEach(javaExtensionRegistry::preprocessor);
    }

    @Override
    public String convert(@NonNull @NotBlank String asciidoc,
                          @NonNull @NotNull File baseDir,
                          @NonNull AsciidocAttributeProvider attributeProvider) {
        attributeProvider.attributes()
                .forEach((name, value) -> attributesBuilder.attribute(name, value));
        Object guide = attributeProvider.attributes().get(ATTRIBUTE_GUIDE);
        if (guide instanceof Guide g) {
            MDC.put("guide", g.getSlug());
        }
        return asciidoctor.convert(asciidoc, optionsBuilder
                .baseDir(baseDir)
                .toFile(false)
                .attributes(attributesBuilder.build())
                .build());
    }
}

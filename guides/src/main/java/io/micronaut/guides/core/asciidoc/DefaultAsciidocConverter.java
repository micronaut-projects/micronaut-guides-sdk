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
import io.micronaut.guides.core.GuideContextProvider;
import io.micronaut.guides.core.asciidoc.extensions.ExtensionsRegistry;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.asciidoctor.*;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.log.Severity;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.util.logging.Logger;

/**
 * DefaultAsciidocConverter is a singleton class that implements the AsciidocConverter interface.
 * It provides methods to convert Asciidoc files to html using Asciidoctor.
 */
@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultAsciidocConverter.class);

    OptionsBuilder optionsBuilder;
    AttributesBuilder attributesBuilder;

    Asciidoctor asciidoctor;

    GuideContextProvider guideContextProvider;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration, ExtensionsRegistry extensionsRegistry, GuideContextProvider guideContextProvider) {
        this.guideContextProvider = guideContextProvider;

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
        extensionsRegistry.getBlockMacroProcessors().forEach(javaExtensionRegistry::blockMacro);
        extensionsRegistry.getBlockProcessors().forEach(javaExtensionRegistry::block);
        extensionsRegistry.getIncludeProcessors().forEach(javaExtensionRegistry::includeProcessor);
        extensionsRegistry.getLineMacroProcessors().forEach(javaExtensionRegistry::inlineMacro);
        extensionsRegistry.getPreProcessors().forEach(javaExtensionRegistry::preprocessor);

        Logger.getLogger("asciidoctor").setUseParentHandlers(false);

        asciidoctor.registerLogHandler(logRecord -> {
            LOG.atLevel(mapSeverity(logRecord.getSeverity())).log("[" + guideContextProvider.getGuide().getSlug() + "] " + (logRecord.getCursor() != null ? logRecord.getCursor().getFile() + " line " + logRecord.getCursor().getLineNumber() : "") + ": " + logRecord.getMessage());
            if (logRecord.getSeverity().ordinal() >= Severity.ERROR.ordinal()) {
                throw new RuntimeException(logRecord.getMessage());
            }
        });
    }

    @Override
    public String convert(@NonNull @NotBlank String asciidoc,
                          @NonNull @NotNull File baseDir,
                          @NonNull @NotBlank String sourceDir,
                          @NonNull @NotBlank String guideSourceDir) {
        return asciidoctor.convert(asciidoc, optionsBuilder
                .baseDir(baseDir)
                .toFile(false)
                .attributes(attributesBuilder
                        .attribute("sourcedir", sourceDir)
                        .attribute("guidesourcedir", guideSourceDir)
                        .attribute("cloud", guideContextProvider.getGuide().getCloud() != null ? guideContextProvider.getGuide().getCloud().getAccronym().toLowerCase() : "")
                        .attribute("cloudName", guideContextProvider.getGuide().getCloud() != null ? guideContextProvider.getGuide().getCloud().getName() : "")
                        .attribute("guideTitle", guideContextProvider.getGuide().getTitle())
                        .attribute("sourceModule", guideContextProvider.getGuide().getSourceModule() != null ? guideContextProvider.getGuide().getSourceModule() + "/" : "")
                        .attribute("sourceBaseModule", guideContextProvider.getGuide().getBaseSourceModule() != null ? guideContextProvider.getGuide().getBaseSourceModule() + "/" : "")
                        .build()
                )
                .build());
    }

    private static Level mapSeverity(Severity severity) {
        return switch (severity) {
            case DEBUG -> Level.DEBUG;
            case WARN -> Level.WARN;
            case ERROR, FATAL -> Level.ERROR;
            default -> Level.INFO;
        };
    }
}

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
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.asciidoctor.*;
import org.asciidoctor.log.Severity;

import java.io.File;
import java.util.logging.Logger;

/**
 * DefaultAsciidocConverter is a singleton class that implements the AsciidocConverter interface.
 * It provides methods to convert Asciidoc files to html using Asciidoctor.
 */
@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    OptionsBuilder optionsBuilder;
    AttributesBuilder attributesBuilder;

    Asciidoctor asciidoctor;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration) {
        attributesBuilder = Attributes.builder()
                .sourceHighlighter(asciidocConfiguration.getSourceHighlighter())
                .tableOfContents(asciidocConfiguration.getToc())
                .attribute("toclevels", asciidocConfiguration.getToclevels())
                .attribute("toc-title", "")
                .sectionNumbers(asciidocConfiguration.getSectnums())
                .attribute("idprefix", asciidocConfiguration.getIdprefix())
                .imagesDir(asciidocConfiguration.getImagesdir())
                .attribute("idseparator", asciidocConfiguration.getIdseparator())
                .icons(asciidocConfiguration.getIcons()).imagesDir(asciidocConfiguration.getImagesdir())
                .noFooter(asciidocConfiguration.isNofooter());

        optionsBuilder = Options.builder()
                .eruby(asciidocConfiguration.getRuby())
                .docType("book")
                .safe(SafeMode.UNSAFE);

        if (StringUtils.isNotEmpty(asciidocConfiguration.getBaseDir())) {
            optionsBuilder.baseDir(new File(asciidocConfiguration.getBaseDir()));
        }

        asciidoctor = Asciidoctor.Factory.create();

        Logger.getLogger("asciidoctor").setUseParentHandlers(false);

        asciidoctor.registerLogHandler(logRecord -> {
            if (logRecord.getSeverity().ordinal() >= Severity.ERROR.ordinal()) {
                throw new RuntimeException(logRecord.getMessage());
            }
            System.out.println("[Asciidoctor] " + logRecord.getSeverity() + " " + logRecord.getSourceFileName() + ": " + logRecord.getMessage());
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
                .attributes(attributesBuilder.attribute("sourcedir", sourceDir).build())
                .attributes(attributesBuilder.attribute("guidesourcedir", guideSourceDir).build())
                .build());
    }
}

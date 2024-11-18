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

import jakarta.inject.Singleton;
import org.asciidoctor.*;

import java.io.File;

@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    OptionsBuilder optionsBuilder;

    Asciidoctor asciidoctor;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration) {
        Attributes attributes = Attributes.builder()
                .attribute("sourcedir", asciidocConfiguration.getSourceDir())
                .attribute("commonsDir", asciidocConfiguration.getCommonsDir())
                .attribute("calloutsDir", asciidocConfiguration.getCalloutsDir())
                .attribute("guidesDir", asciidocConfiguration.getGuidesDir())
                .sourceHighlighter(asciidocConfiguration.getSourceHighlighter())
                .tableOfContents(asciidocConfiguration.getToc())
                .attribute("toclevels", asciidocConfiguration.getToclevels())
                .sectionNumbers(asciidocConfiguration.getSectnums())
                .attribute("idprefix", asciidocConfiguration.getIdprefix())
                .attribute("idseparator", asciidocConfiguration.getIdseparator())
                .icons(asciidocConfiguration.getIcons())
                .imagesDir(asciidocConfiguration.getImagesdir())
                .noFooter(asciidocConfiguration.isNofooter())
                .build();

        optionsBuilder = Options.builder()
                .docType(asciidocConfiguration.getDocType())
                .eruby(asciidocConfiguration.getRuby())
                .templateDirs(asciidocConfiguration.getTemplateDirs())
                .attributes(attributes)
                .safe(SafeMode.UNSAFE)
                .baseDir(new File(asciidocConfiguration.getBaseDir()));

        asciidoctor = Asciidoctor.Factory.create();
    }

    @Override
    public void convert(File source, File destination) {
        asciidoctor.convertFile(source, optionsBuilder.toFile(destination).build());
    }

    @Override
    public String convert(File source) {
        return asciidoctor.convertFile(source, optionsBuilder.toFile(false).build());
    }
}

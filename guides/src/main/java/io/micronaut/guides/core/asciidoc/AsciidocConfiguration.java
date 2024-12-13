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

import org.asciidoctor.Placement;

import java.io.File;

/**
 * AsciidocConfiguration is an interface that defines the configuration properties for Asciidoc processing.
 */
public interface AsciidocConfiguration {
    /**
     * Returns the source highlighter.
     *
     * @return the source highlighter
     */
    String getSourceHighlighter();

    /**
     * Returns the table of contents (TOC) placement.
     *
     * @return the TOC placement
     */
    Placement getToc();

    /**
     * Returns the number of TOC levels.
     *
     * @return the number of TOC levels
     */
    int getToclevels();

    /**
     * Returns whether section numbers are enabled.
     *
     * @return true if section numbers are enabled, false otherwise
     */
    boolean getSectnums();

    /**
     * Returns the ID prefix.
     *
     * @return the ID prefix
     */
    String getIdprefix();

    /**
     * Returns the ID separator.
     *
     * @return the ID separator
     */
    String getIdseparator();

    /**
     * Returns the icons configuration.
     *
     * @return the icons configuration
     */
    String getIcons();

    /**
     * Returns the images directory.
     *
     * @return the images directory
     */
    String getImagesdir();

    /**
     * Returns whether the footer is disabled.
     *
     * @return true if the footer is disabled, false otherwise
     */
    boolean isNofooter();

    /**
     * Returns the document type.
     *
     * @return the document type
     */
    String getDocType();

    /**
     * Returns the Ruby interpreter configuration.
     *
     * @return the Ruby interpreter configuration
     */
    String getRuby();

    /**
     * Returns the template directories.
     *
     * @return the template directories
     */
    File getTemplateDirs();

    /**
     * Returns the common resources directory.
     *
     * @return the common resources directory
     */
    String getCommonsDir();

    /**
     * Returns the base directory.
     *
     * @return the base directory
     */
    String getBaseDir();

    /**
     * Returns the callouts directory.
     *
     * @return the callouts directory
     */
    String getCalloutsDir();
}

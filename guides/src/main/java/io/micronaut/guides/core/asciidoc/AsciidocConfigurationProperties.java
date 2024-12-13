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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Internal;
import org.asciidoctor.Placement;

import java.io.File;

/**
 * AsciidocConfigurationProperties is a class that implements the AsciidocConfiguration interface.
 * It is annotated with @Internal and @ConfigurationProperties to define configuration properties for Asciidoc processing.
 */
@Internal
@ConfigurationProperties(AsciidocConfigurationProperties.PREFIX)
public class AsciidocConfigurationProperties implements AsciidocConfiguration {
    public static final String PREFIX = "asciidoc";
    private static final String DEFAULT_SOURCE_HIGHLIGHTER = "coderay";
    private static final Placement DEFAULT_TOC = Placement.LEFT;
    private static final int DEFAULT_TOCLEVELS = 1;
    private static final boolean DEFAULT_SECTNUMS = true;
    private static final String DEFAULT_IDPREFIX = "";
    private static final String DEFAULT_IDSEPARATOR = "-";
    private static final String DEFAULT_ICONS = "font";
    private static final String DEFAULT_IMAGESDIR = "images";
    private static final boolean DEFAULT_NOFOOTER = true;
    private static final String DEFAULT_DOCTYPE = "book";
    private static final String DEFAULT_RUBY = "erubis";
    private static final String DEFAULT_TEMPLATE_DIRS = "src/docs/asciidoc";
    private static final String DEFAULT_COMMONS_DIR = "src/docs/asciidoc/common";
    private static final String DEFAULT_CALLOUTS_DIR = "src/docs/asciidoc/callouts";
    private static final String DEFAULT_BASE_DIR = "";

    private String sourceHighlighter = DEFAULT_SOURCE_HIGHLIGHTER;
    private Placement toc = DEFAULT_TOC;
    private int toclevels = DEFAULT_TOCLEVELS;
    private boolean sectnums = DEFAULT_SECTNUMS;
    private String idprefix = DEFAULT_IDPREFIX;
    private String idseparator = DEFAULT_IDSEPARATOR;
    private String icons = DEFAULT_ICONS;
    private String imagesdir = DEFAULT_IMAGESDIR;
    private boolean nofooter = DEFAULT_NOFOOTER;
    private String docType = DEFAULT_DOCTYPE;
    private String ruby = DEFAULT_RUBY;
    private File templateDirs = new File(DEFAULT_TEMPLATE_DIRS);
    private String commonsDir = DEFAULT_COMMONS_DIR;
    private String baseDir = DEFAULT_BASE_DIR;
    private String calloutsDir = DEFAULT_CALLOUTS_DIR;

    /**
     * Gets the source highlighter.
     *
     * @return the source highlighter
     */
    @Override
    public String getSourceHighlighter() {
        return sourceHighlighter;
    }

    /**
     * Sets the source highlighter.
     *
     * @param sourceHighlighter the source highlighter to set
     */
    public void setSourceHighlighter(String sourceHighlighter) {
        this.sourceHighlighter = sourceHighlighter;
    }

    /**
     * Gets the table of contents (TOC) placement.
     *
     * @return the TOC placement
     */
    @Override
    public Placement getToc() {
        return toc;
    }

    /**
     * Sets the table of contents (TOC) placement.
     *
     * @param toc the TOC placement to set
     */
    public void setToc(Placement toc) {
        this.toc = toc;
    }

    /**
     * Gets the number of TOC levels.
     *
     * @return the number of TOC levels
     */
    @Override
    public int getToclevels() {
        return toclevels;
    }

    /**
     * Sets the number of TOC levels.
     *
     * @param toclevels the number of TOC levels to set
     */
    public void setToclevels(int toclevels) {
        this.toclevels = toclevels;
    }

    /**
     * Gets the section numbers.
     *
     * @return true if section numbers are enabled, false otherwise
     */
    @Override
    public boolean getSectnums() {
        return sectnums;
    }

    /**
     * Sets the section numbers.
     *
     * @param sectnums true to enable section numbers, false to disable
     */
    public void setSectnums(boolean sectnums) {
        this.sectnums = sectnums;
    }

    /**
     * Gets the ID prefix.
     *
     * @return the ID prefix
     */
    @Override
    public String getIdprefix() {
        return idprefix;
    }

    /**
     * Sets the ID prefix.
     *
     * @param idprefix the ID prefix to set
     */
    public void setIdprefix(String idprefix) {
        this.idprefix = idprefix;
    }

    /**
     * Gets the ID separator.
     *
     * @return the ID separator
     */
    @Override
    public String getIdseparator() {
        return idseparator;
    }

    /**
     * Sets the ID separator.
     *
     * @param idseparator the ID separator to set
     */
    public void setIdseparator(String idseparator) {
        this.idseparator = idseparator;
    }

    /**
     * Gets the icons.
     *
     * @return the icons
     */
    @Override
    public String getIcons() {
        return icons;
    }

    /**
     * Sets the icons.
     *
     * @param icons the icons to set
     */
    public void setIcons(String icons) {
        this.icons = icons;
    }

    /**
     * Gets the images directory.
     *
     * @return the images directory
     */
    @Override
    public String getImagesdir() {
        return imagesdir;
    }

    /**
     * Sets the images directory.
     *
     * @param imagesdir the images directory to set
     */
    public void setImagesdir(String imagesdir) {
        this.imagesdir = imagesdir;
    }

    /**
     * Checks if the footer is disabled.
     *
     * @return true if the footer is disabled, false otherwise
     */
    @Override
    public boolean isNofooter() {
        return nofooter;
    }

    /**
     * Sets the footer visibility.
     *
     * @param nofooter true to disable the footer, false to enable
     */
    public void setNofooter(boolean nofooter) {
        this.nofooter = nofooter;
    }

    /**
     * Gets the document type.
     *
     * @return the document type
     */
    @Override
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the document type.
     *
     * @param docType the document type to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * Gets the Ruby engine.
     *
     * @return the Ruby engine
     */
    @Override
    public String getRuby() {
        return ruby;
    }

    /**
     * Sets the Ruby engine.
     *
     * @param ruby the Ruby engine to set
     */
    public void setRuby(String ruby) {
        this.ruby = ruby;
    }

    /**
     * Gets the template directories.
     *
     * @return the template directories
     */
    @Override
    public File getTemplateDirs() {
        return templateDirs;
    }

    /**
     * Sets the template directories.
     *
     * @param templateDirs the template directories to set
     */
    public void setTemplateDirs(File templateDirs) {
        this.templateDirs = templateDirs;
    }

    /**
     * Gets the commons directory.
     *
     * @return the commons directory
     */
    @Override
    public String getCommonsDir() {
        return commonsDir;
    }

    /**
     * Sets the commons directory.
     *
     * @param commonsDir the commons directory to set
     */
    public void setCommonsDir(String commonsDir) {
        this.commonsDir = commonsDir;
    }

    /**
     * Gets the base directory.
     *
     * @return the base directory
     */
    @Override
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * Sets the base directory.
     *
     * @param baseDir the base directory to set
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Gets the callouts directory.
     *
     * @return the callouts directory
     */
    @Override
    public String getCalloutsDir() {
        return calloutsDir;
    }

    /**
     * Sets the callouts directory.
     *
     * @param calloutsDir the callouts directory to set
     */
    public void setCalloutsDir(String calloutsDir) {
        this.calloutsDir = calloutsDir;
    }
}

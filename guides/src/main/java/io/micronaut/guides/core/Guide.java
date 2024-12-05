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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static io.micronaut.guides.core.GuideUtils.FEATURES_PREFIXES;
import static io.micronaut.guides.core.GuideUtils.addAllSafe;

/**
 * Represents a guide metadata.
 **/
@Serdeable
public class Guide {
    @JsonPropertyDescription("The guide's title")
    @NonNull
    @NotBlank
    private String title;

    @JsonPropertyDescription("The guide introduction")
    @NonNull
    @NotBlank
    private String intro;

    @JsonPropertyDescription("The guide's authors")
    @Nullable
    private List<String> authors;

    @JsonPropertyDescription("The guide's categories")
    @NotEmpty
    @NonNull
    private List<String> categories;

    @JsonPropertyDescription("The guide publication date. It should follow the format YYYY-MM-DD")
    @NonNull
    @NotNull
    private LocalDate publicationDate;

    @JsonPropertyDescription("If the guide needs a minimum Java version, define it here")
    @Nullable
    private Integer minimumJavaVersion;

    @JsonPropertyDescription("If the guide needs a maximum Java version, define it here")
    @Nullable
    private Integer maximumJavaVersion;

    @JsonPropertyDescription("The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure")
    @Nullable
    private Cloud cloud;

    @JsonPropertyDescription("Set it to true to skip running the tests for the Gradle applications for the guide")
    @JsonProperty(defaultValue = StringUtils.FALSE)
    @Nullable
    private Boolean skipGradleTests;

    @JsonPropertyDescription("Set it to true to skip running the tests for the Maven applications for the guide")
    @JsonProperty(defaultValue = StringUtils.FALSE)
    @Nullable
    private Boolean skipMavenTests;

    @JsonPropertyDescription("The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used")
    @Nullable
    private String asciidoctor;

    @JsonPropertyDescription("The guide supported languages")
    @Nullable
    private List<Language> languages;

    @JsonPropertyDescription("List of tags added to the guide. Features are added automatically as tags. No need to repeat them here")
    @Nullable
    private List<String> tags;

    @JsonPropertyDescription("By default the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here")
    @Nullable
    private List<BuildTool> buildTools;

    @JsonPropertyDescription("The guide's test framework. By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock")
    @Nullable
    private TestFramework testFramework;

    @JsonPropertyDescription("List of additional files with a relative path to include in the generated zip file for the guide")
    @Nullable
    private List<String> zipIncludes;

    @JsonPropertyDescription("The guide's slug. If not specified, the guides folder is used")
    @Nullable
    private String slug;

    @JsonIgnore
    @Nullable
    private File folder;

    @JsonPropertyDescription("Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides")
    @Nullable
    @JsonProperty(defaultValue = StringUtils.TRUE)
    private Boolean publish;

    @JsonPropertyDescription("Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one")
    @Nullable
    private String base;

    @JsonPropertyDescription("The guide's environment variables")
    @Nullable
    private Map<String, String> env;

    @JsonPropertyDescription("Applications created for the guide")
    @NotEmpty
    @NonNull
    private List<? extends App> apps;

    /**
     * Gets the title of the guide.
     *
     * @return The title of the guide.
     */
    public @NonNull @NotBlank String getTitle() {
        return title;
    }

    /**
     * Sets the title of the guide.
     *
     * @param title The title to set.
     */
    public void setTitle(@NonNull @NotBlank String title) {
        this.title = title;
    }

    /**
     * Gets the introduction of the guide.
     *
     * @return The introduction of the guide.
     */
    public @NonNull @NotBlank String getIntro() {
        return intro;
    }

    /**
     * Sets the introduction of the guide.
     *
     * @param intro The introduction to set.
     */
    public void setIntro(@NonNull @NotBlank String intro) {
        this.intro = intro;
    }

    /**
     * Gets the authors of the guide.
     *
     * @return The list of authors.
     */
    @Nullable
    public List<String> getAuthors() {
        return authors != null ? authors : Collections.emptyList();
    }

    /**
     * Sets the authors of the guide.
     *
     * @param authors The list of authors to set.
     */
    public void setAuthors(@Nullable List<String> authors) {
        this.authors = authors;
    }

    /**
     * Gets the categories of the guide.
     *
     * @return The list of categories.
     */
    public @NotEmpty @NonNull List<String> getCategories() {
        return categories != null ? categories : Collections.emptyList();
    }

    /**
     * Sets the categories of the guide.
     *
     * @param categories The list of categories to set.
     */
    public void setCategories(@NotEmpty @NonNull List<String> categories) {
        this.categories = categories;
    }

    /**
     * Gets the publication date of the guide.
     *
     * @return The publication date.
     */
    public @NonNull @NotNull LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the publication date of the guide.
     *
     * @param publicationDate The publication date to set.
     */
    public void setPublicationDate(@NonNull @NotNull LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Gets the minimum Java version required for the guide.
     *
     * @return The minimum Java version, or null if not specified.
     */
    public @Nullable Integer getMinimumJavaVersion() {
        return minimumJavaVersion;
    }

    /**
     * Sets the minimum Java version required for the guide.
     *
     * @param minimumJavaVersion The minimum Java version to set.
     */
    public void setMinimumJavaVersion(@Nullable Integer minimumJavaVersion) {
        this.minimumJavaVersion = minimumJavaVersion;
    }

    /**
     * Gets the maximum Java version required for the guide.
     *
     * @return The maximum Java version, or null if not specified.
     */
    public @Nullable Integer getMaximumJavaVersion() {
        return maximumJavaVersion;
    }

    /**
     * Sets the maximum Java version required for the guide.
     *
     * @param maximumJavaVersion The maximum Java version to set.
     */
    public void setMaximumJavaVersion(@Nullable Integer maximumJavaVersion) {
        this.maximumJavaVersion = maximumJavaVersion;
    }

    /**
     * Gets the cloud service provider for the guide.
     *
     * @return The cloud service provider, or null if not specified.
     */
    public @Nullable Cloud getCloud() {
        return cloud;
    }

    /**
     * Sets the cloud service provider for the guide.
     *
     * @param cloud The cloud service provider to set.
     */
    public void setCloud(@Nullable Cloud cloud) {
        this.cloud = cloud;
    }

    /**
     * Checks if Gradle tests should be skipped for the guide.
     *
     * @return True if Gradle tests should be skipped, false otherwise.
     */
    public @Nullable Boolean isSkipGradleTests() {
        return skipGradleTests != null ? skipGradleTests : Boolean.FALSE;
    }

    /**
     * Sets whether Gradle tests should be skipped for the guide.
     *
     * @param skipGradleTests True to skip Gradle tests, false otherwise.
     */
    public void setSkipGradleTests(@Nullable Boolean skipGradleTests) {
        this.skipGradleTests = skipGradleTests;
    }

    /**
     * Checks if Maven tests should be skipped for the guide.
     *
     * @return True if Maven tests should be skipped, false otherwise.
     */
    public @Nullable Boolean isSkipMavenTests() {
        return skipMavenTests != null ? skipMavenTests : Boolean.FALSE;
    }

    /**
     * Sets whether Maven tests should be skipped for the guide.
     *
     * @param skipMavenTests True to skip Maven tests, false otherwise.
     */
    public void setSkipMavenTests(@Nullable Boolean skipMavenTests) {
        this.skipMavenTests = skipMavenTests;
    }

    /**
     * Gets the Asciidoctor file for the guide.
     *
     * @return The Asciidoctor file, or null if not specified.
     */
    public @Nullable String getAsciidoctor() {
        return asciidoctor;
    }

    /**
     * Sets the Asciidoctor file for the guide.
     *
     * @param asciidoctor The Asciidoctor file to set.
     */
    public void setAsciidoctor(@Nullable String asciidoctor) {
        this.asciidoctor = asciidoctor;
    }

    /**
     * Gets the supported languages for the guide.
     *
     * @return The list of supported languages, or null if not specified.
     */
    public @Nullable List<Language> getLanguages() {
        return languages;
    }

    /**
     * Sets the supported languages for the guide.
     *
     * @param languages The list of supported languages to set.
     */
    public void setLanguages(@Nullable List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Gets the tags for the guide.
     *
     * @return The list of tags, or null if not specified.
     */
    public @Nullable List<String> getTags() {
        Set<String> tagsList = new HashSet<>();
        if (tags != null) {
            addAllSafe(tagsList, tags);
        }
        for (App app : getApps()) {
            List<String> allFeatures = new ArrayList<>();
            addAllSafe(allFeatures, app.getFeatures());
            addAllSafe(allFeatures, app.getJavaFeatures());
            addAllSafe(allFeatures, app.getKotlinFeatures());
            addAllSafe(allFeatures, app.getGroovyFeatures());
            for (String featureName : allFeatures) {
                String tagToAdd = featureName;
                for (String prefix : FEATURES_PREFIXES) {
                    if (tagToAdd.startsWith(prefix)) {
                        tagToAdd = tagToAdd.substring(prefix.length());
                    }
                }
                tagsList.add(tagToAdd);
            }
        }
        Set<String> categoriesAsTags = getCategories().stream().map(String::toLowerCase).map(s -> s.replace(" ", "-")).collect(Collectors.toSet());
        tagsList.addAll(categoriesAsTags);
        return new ArrayList<>(tagsList);
    }

    /**
     * Sets the tags for the guide.
     *
     * @param tags The list of tags to set.
     */
    public void setTags(@Nullable List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gets the build tools for the guide.
     *
     * @return The list of build tools, or null if not specified.
     */
    public @Nullable List<BuildTool> getBuildTools() {
        return buildTools == null
                ? List.of(BuildTool.GRADLE, BuildTool.MAVEN)
                : buildTools;
    }

    /**
     * Sets the build tools for the guide.
     *
     * @param buildTools The list of build tools to set.
     */
    public void setBuildTools(@Nullable List<BuildTool> buildTools) {
        this.buildTools = buildTools;
    }

    /**
     * Gets the test framework for the guide.
     *
     * @return The test framework, or null if not specified.
     */
    public @Nullable TestFramework getTestFramework() {
        return testFramework;
    }

    /**
     * Sets the test framework for the guide.
     *
     * @param testFramework The test framework to set.
     */
    public void setTestFramework(@Nullable TestFramework testFramework) {
        this.testFramework = testFramework;
    }

    /**
     * Gets the additional files to include in the generated zip file for the guide.
     *
     * @return The list of additional files, or null if not specified.
     */
    public @Nullable List<String> getZipIncludes() {
        return zipIncludes != null ? zipIncludes : Collections.emptyList();
    }

    /**
     * Sets the additional files to include in the generated zip file for the guide.
     *
     * @param zipIncludes The list of additional files to set.
     */
    public void setZipIncludes(@Nullable List<String> zipIncludes) {
        this.zipIncludes = zipIncludes;
    }

    /**
     * Gets the slug for the guide.
     *
     * @return The slug, or null if not specified.
     */
    public @Nullable String getSlug() {
        return slug;
    }

    /**
     * Sets the slug for the guide.
     *
     * @param slug The slug to set.
     */
    public void setSlug(@Nullable String slug) {
        this.slug = slug;
    }

    /**
     * Sets the slug for the guide.
     *
     * @param folder The folder to set.
     */
    public void setFolder(@Nullable File folder) {
        this.folder = folder;
    }

    /**
     * Gets the folder for the guide.
     *
     * @return The folder, or null if not specified.
     */
    public @Nullable File getFolder() {
        return folder;
    }

    /**
     * Checks if the guide should be published.
     *
     * @return True if the guide should be published, false otherwise.
     */
    public @Nullable Boolean isPublish() {
        return publish != null ? publish : Boolean.TRUE;

    }

    /**
     * Sets whether the guide should be published.
     *
     * @param publish True to publish the guide, false otherwise.
     */
    public void setPublish(@Nullable Boolean publish) {
        this.publish = publish;
    }

    /**
     * Gets the base guide slug name.
     *
     * @return The base guide directory name, or null if not specified.
     */
    public @Nullable String getBase() {
        return base;
    }

    /**
     * Sets the base guide slug name.
     *
     * @param base The base guide directory name to set.
     */
    public void setBase(@Nullable String base) {
        this.base = base;
    }

    /**
     * Gets the environment variables for the guide.
     *
     * @return The map of environment variables, or null if not specified.
     */
    public @Nullable Map<String, String> getEnv() {
        return env == null ? Collections.emptyMap() : env;
    }

    /**
     * Sets the environment variables for the guide.
     *
     * @param env The map of environment variables to set.
     */
    public void setEnv(@Nullable Map<String, String> env) {
        this.env = env;
    }

    /**
     * Gets the applications created for the guide.
     *
     * @return The list of applications.
     */
    @NotEmpty
    @NonNull
    public List<? extends App> getApps() {
        return apps;
    }

    /**
     * Sets the applications created for the guide.
     *
     * @param apps The list of applications to set.
     */
    public void setApps(@NotEmpty @NonNull List<? extends App> apps) {
        this.apps = apps;
    }

    /**
     * Determines if a guide should skip tests based on the build tool.
     *
     * @param buildTool The build tool to check against.
     * @return True if the guide should skip tests, false otherwise.
     */
    public boolean shouldSkip(BuildTool buildTool) {
        if (BuildTool.valuesGradle().contains(buildTool)) {
            return isSkipGradleTests();
        }
        if (buildTool == BuildTool.MAVEN) {
            return isSkipMavenTests();
        }
        return false;
    }

    /**
     * Retrieves a set of frameworks used in a given guide.
     *
     * @return A set of frameworks associated with the guide.
     */
    public Set<String> getFrameworks() {
        return getApps().stream().map(App::getFramework).collect(Collectors.toSet());
    }
}

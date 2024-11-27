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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.guides.core.GuideMerger.mergeLists;
import static io.micronaut.guides.core.GuideUtils.addAllSafe;

/**
 * Represents an application metadata.
 */
@Serdeable
@JsonSchema
public class App {
    public static final String SPOTLESS = "spotless";

    @JsonPropertyDescription("The app's name. For single application guides, the application needs to be named default")
    @NonNull
    @NotBlank
    private String name;

    @JsonPropertyDescription("The app's package name. If you don't specify, the package name example.micronaut is used")
    @JsonProperty(defaultValue = "example.micronaut")
    @Nullable
    private String packageName;

    @JsonPropertyDescription("The app type. If you don't specify, default is used")
    @JsonProperty(defaultValue = "DEFAULT")
    @Nullable
    private ApplicationType applicationType;

    @JsonPropertyDescription("The app's framework. Default is Micronaut but Spring Boot is also supported")
    @JsonProperty(defaultValue = "Micronaut")
    @Nullable
    private String framework;

    @JsonPropertyDescription("The Micronaut Starter features' name that the app requires")
    @Nullable
    private List<String> features = new ArrayList<>();

    @JsonPropertyDescription("The app's invisible features")
    @Nullable
    private List<String> invisibleFeatures = new ArrayList<>();

    @JsonPropertyDescription("The app's Kotlin features")
    @Nullable
    private List<String> kotlinFeatures = new ArrayList<>();

    @JsonPropertyDescription("The app's Java features")
    @Nullable
    private List<String> javaFeatures = new ArrayList<>();

    @JsonPropertyDescription("The app's Groovy features")
    @Nullable
    private List<String> groovyFeatures = new ArrayList<>();

    @JsonPropertyDescription("The app's test framework")
    @Nullable
    private TestFramework testFramework;

    @JsonPropertyDescription("The tests that should not be run")
    @Nullable
    private List<String> excludeTest;

    @JsonPropertyDescription("The source files that should not be included")
    @Nullable
    private List<String> excludeSource;

    @JsonPropertyDescription("To enable Spotless code check")
    @JsonProperty(defaultValue = StringUtils.TRUE)
    @Nullable
    private Boolean validateLicense;

    /**
     * Gets the name of the app.
     *
     * @return The name of the app.
     */
    public @NonNull @NotBlank String getName() {
        return name;
    }

    /**
     * Sets the name of the app.
     *
     * @param name The name to set.
     */
    public void setName(@NonNull @NotBlank String name) {
        this.name = name;
    }

    /**
     * Gets the package name of the app.
     *
     * @return The package name of the app, or null if not specified.
     */
    public @Nullable String getPackageName() {
        return packageName;
    }

    /**
     * Sets the package name of the app.
     *
     * @param packageName The package name to set.
     */
    public void setPackageName(@Nullable String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets the application type of the app.
     *
     * @return The application type of the app, or null if not specified.
     */
    public @Nullable ApplicationType getApplicationType() {
        return applicationType;
    }

    /**
     * Sets the application type of the app.
     *
     * @param applicationType The application type to set.
     */
    public void setApplicationType(@Nullable ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * Gets the framework of the app.
     *
     * @return The framework of the app, or null if not specified.
     */
    public @Nullable String getFramework() {
        return framework;
    }

    /**
     * Sets the framework of the app.
     *
     * @param framework The framework to set.
     */
    public void setFramework(@Nullable String framework) {
        this.framework = framework;
    }

    /**
     * Gets the features of the app.
     *
     * @return The list of features, or null if not specified.
     */
    public @Nullable List<String> getFeatures() {
        return features;
    }

    /**
     * Sets the features of the app.
     *
     * @param features The list of features to set.
     */
    public void setFeatures(@Nullable List<String> features) {
        this.features = features;
    }

    /**
     * Sets the invisible features of the app.
     *
     * @param invisibleFeatures The list of invisible features to set.
     */
    public void setInvisibleFeatures(@Nullable List<String> invisibleFeatures) {
        this.invisibleFeatures = invisibleFeatures;
    }

    /**
     * Gets the Kotlin features of the app.
     *
     * @return The list of Kotlin features, or null if not specified.
     */
    public @Nullable List<String> getKotlinFeatures() {
        return kotlinFeatures;
    }

    /**
     * Sets the Kotlin features of the app.
     *
     * @param kotlinFeatures The list of Kotlin features to set.
     */
    public void setKotlinFeatures(@Nullable List<String> kotlinFeatures) {
        this.kotlinFeatures = kotlinFeatures;
    }

    /**
     * Gets the Java features of the app.
     *
     * @return The list of Java features, or null if not specified.
     */
    public @Nullable List<String> getJavaFeatures() {
        return javaFeatures;
    }

    /**
     * Sets the Java features of the app.
     *
     * @param javaFeatures The list of Java features to set.
     */
    public void setJavaFeatures(@Nullable List<String> javaFeatures) {
        this.javaFeatures = javaFeatures;
    }

    /**
     * Gets the Groovy features of the app.
     *
     * @return The list of Groovy features, or null if not specified.
     */
    public @Nullable List<String> getGroovyFeatures() {
        return groovyFeatures;
    }

    /**
     * Sets the Groovy features of the app.
     *
     * @param groovyFeatures The list of Groovy features to set.
     */
    public void setGroovyFeatures(@Nullable List<String> groovyFeatures) {
        this.groovyFeatures = groovyFeatures;
    }

    /**
     * Gets the test framework of the app.
     *
     * @return The test framework, or null if not specified.
     */
    public @Nullable TestFramework getTestFramework() {
        return testFramework;
    }

    /**
     * Sets the test framework of the app.
     *
     * @param testFramework The test framework to set.
     */
    public void setTestFramework(@Nullable TestFramework testFramework) {
        this.testFramework = testFramework;
    }

    /**
     * Gets the tests that should not be run.
     *
     * @return The list of tests to exclude, or null if not specified.
     */
    public @Nullable List<String> getExcludeTest() {
        return excludeTest;
    }

    /**
     * Sets the tests that should not be run.
     *
     * @param excludeTest The list of tests to exclude.
     */
    public void setExcludeTest(@Nullable List<String> excludeTest) {
        this.excludeTest = excludeTest;
    }

    /**
     * Gets the source files that should not be included.
     *
     * @return The list of source files to exclude, or null if not specified.
     */
    public @Nullable List<String> getExcludeSource() {
        return excludeSource;
    }

    /**
     * Sets the source files that should not be included.
     *
     * @param excludeSource The list of source files to exclude.
     */
    public void setExcludeSource(@Nullable List<String> excludeSource) {
        this.excludeSource = excludeSource;
    }

    /**
     * Checks if the license validation is enabled.
     *
     * @return True if license validation is enabled, false otherwise.
     */
    public @Nullable Boolean isValidateLicense() {
        return validateLicense != null ? validateLicense && hasSpotless(this.features, this.invisibleFeatures, this.kotlinFeatures, this.javaFeatures, this.groovyFeatures) : Boolean.FALSE;
    }

    /**
     * Sets whether the license validation is enabled.
     *
     * @param validateLicense True to enable license validation, false otherwise.
     */
    public void setValidateLicense(@Nullable Boolean validateLicense) {
        this.validateLicense = validateLicense;
    }

    /**
     * Checks if the Spotless feature is present in the given feature lists.
     *
     * @param featureLists The feature lists to check.
     * @return True if the Spotless feature is present, false otherwise.
     */
    @SafeVarargs
    private boolean hasSpotless(List<String>... featureLists) {
        for (List<String> features : featureLists) {
            if (features != null && features.contains(SPOTLESS)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a list of features for a given app and language.
     *
     * @param language The language for which to retrieve features.
     * @return A list of features associated with the app and language.
     */
    public List<String> features(Language language) {
        if (language == Language.JAVA) {
            return mergeLists(features, invisibleFeatures(), getJavaFeatures());
        }
        if (language == Language.KOTLIN) {
            return mergeLists(features, invisibleFeatures(), getKotlinFeatures());
        }
        if (language == Language.GROOVY) {
            return mergeLists(features, invisibleFeatures(), getGroovyFeatures());
        }
        return mergeLists(features, invisibleFeatures());
    }

    /**
     * Retrieves a list of invisible features for a given app.
     *
     * @return A list of invisible features associated with the app.
     */
    public List<String> invisibleFeatures() {
        if (isValidateLicense()) {
            List<String> result = new ArrayList<>();
            addAllSafe(result, invisibleFeatures);
            return result;
        }
        return invisibleFeatures;
    }

    /**
     * Retrieves a list of visible features for a given app and language.
     *
     * @param language The language for which to retrieve visible features.
     * @return A list of visible features associated with the app and language.
     */
    public List<String> visibleFeatures(Language language) {
        if (language == Language.JAVA) {
            return mergeLists(features, getJavaFeatures());
        }
        if (language == Language.KOTLIN) {
            return mergeLists(features, getKotlinFeatures());
        }
        if (language == Language.GROOVY) {
            return mergeLists(features, getGroovyFeatures());
        }
        return features;
    }
}



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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;

import java.util.List;

/**
 * GuidesConfiguration is an interface that defines the configuration settings for guides.
 */
public interface GuidesConfiguration {
    @NonNull
    List<Language> getDefaultLanguages();

    /**
     * Returns the guides directory.
     *
     * @return the guides directory
     */
    String getGuidesDir();

    /**
     * Returns the home page URL.
     *
     * @return the home page URL
     */
    String getHomePageUrl();

    /**
     * Returns the title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Returns the license path.
     *
     * @return the license path
     */
    String getLicensePath();

    /**
     * Returns the package name.
     *
     * @return the package name
     */
    String getPackageName();

    /**
     * Returns the default application name.
     *
     * @return the default application name
     */
    String getDefaultAppName();

    /**
     * Returns the project generator URL.
     *
     * @return the project generator URL
     */
    String getProjectGeneratorUrl();

    /**
     * Returns the default minimum JDK version.
     *
     * @return the default minimum JDK version
     */
    int getDefaultMinJdk();

    /**
     * Returns the API URL.
     *
     * @return the API URL
     */
    String getApiUrl();

    /**
     * Returns the version path.
     *
     * @return the version path
     */
    String getVersion();

    /**
     * Returns the environment JDK version.
     *
     * @return the environment JDK version
     */
    String getEnvJdkVersion();

    /**
     * Returns the list of files with headers.
     *
     * @return the list of files with headers
     */
    List<String> getFilesWithHeader();

    /**
     * Returns the default JDK version.
     *
     * @return the default JDK version
     */
    JdkVersion getDefaultJdkVersion();

    /**
     * Returns the list of JDK versions supported by GraalVM.
     *
     * @return the list of JDK versions supported by GraalVM
     */
    List<JdkVersion> getJdkVersionsSupportedByGraalvm();

    /**
     * Returns the GitHub workflow Java CI configuration.
     *
     * @return the GitHub workflow Java CI configuration
     */
    String getGithubWorkflowJavaCi();

    /**
     * Returns the environment GitHub workflow configuration.
     *
     * @return the environment GitHub workflow configuration
     */
    String getEnvGithubWorkflow();

    /**
     * Returns the system property for Micronaut guide.
     *
     * @return the system property for Micronaut guide
     */
    String getSysPropMicronautGuide();

    /**
     *
     * @return Whether the Guide metadata should be validated against the JSON Schema
     */
    boolean isValidateMetadata();

    @NonNull
    List<String> getZipIncludesExtensions();
}

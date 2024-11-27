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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.starter.options.JdkVersion;

import java.util.List;

import static io.micronaut.starter.options.JdkVersion.JDK_17;
import static io.micronaut.starter.options.JdkVersion.JDK_21;

/**
 * GuidesConfigurationProperties is a class that implements the GuidesConfiguration interface.
 * It is annotated with @ConfigurationProperties to bind the properties with the specified prefix.
 */
@ConfigurationProperties(GuidesConfigurationProperties.PREFIX)
public class GuidesConfigurationProperties implements GuidesConfiguration {
    public static final String PREFIX = "guides";
    public static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private static final String DEFAULT_LICENSEHEADER = "classpath:LICENSEHEADER";
    private static final String DEFAULT_PACKAGE_NAME = "example.micronaut";
    private static final String DEFAULT_APP_NAME = "default";
    private static final String HOMEPAGE_URL = "https://micronaut.io";
    private static final String LAUNCHER_URL = HOMEPAGE_URL + "/launch";
    private static final int DEFAULT_MIN_JDK = 17;
    private static final String API_URL = "https://docs.micronaut.io/latest/api";
    private static final String DEFAULT_VERSION = "0.0.0";
    private static final String DEFAULT_ENV_JDK_VERSION = "JDK_VERSION";
    private static final JdkVersion DEFAULT_JAVA_VERSION = JDK_17;
    private static final List<JdkVersion> DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM = List.of(JDK_17, JDK_21);
    private static final String GITHUB_WORKFLOW_JAVA_CI = "Java CI";
    private static final String ENV_GITHUB_WORKFLOW = "GITHUB_WORKFLOW";
    private static final String SYS_PROP_MICRONAUT_GUIDE = "micronaut.guide";
    private static final String DEFAULT_GUIDES_DIR = "guides";

    private String guidesDir = DEFAULT_GUIDES_DIR;
    private String title = "Micronaut Guides";
    private String homePageUrl = GUIDES_URL;
    private String licensePath = DEFAULT_LICENSEHEADER;
    private String packageName = DEFAULT_PACKAGE_NAME;
    private List<String> sourceFilesExtensions = List.of("java", "kotlin", "groovy");
    private String envJdkVersion = DEFAULT_ENV_JDK_VERSION;
    private JdkVersion defaulJdkVersion = DEFAULT_JAVA_VERSION;
    private List<JdkVersion> jdkVersionsSupportedByGraalvm = DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM;
    private String githubWorkflowJavaCi = GITHUB_WORKFLOW_JAVA_CI;
    private String envGithubWorkflow = ENV_GITHUB_WORKFLOW;
    private String sysPropMicronautGuide = SYS_PROP_MICRONAUT_GUIDE;
    private String version = DEFAULT_VERSION;

    /**
     * Gets the JDK versions supported by GraalVM.
     *
     * @return the list of JDK versions supported by GraalVM
     */
    @Override
    public List<JdkVersion> getJdkVersionsSupportedByGraalvm() {
        return jdkVersionsSupportedByGraalvm;
    }

    /**
     * Sets the JDK versions supported by GraalVM.
     *
     * @param jdkVersionsSupportedByGraalvm the list of JDK versions to set
     */
    public void setJdkVersionsSupportedByGraalvm(List<JdkVersion> jdkVersionsSupportedByGraalvm) {
        this.jdkVersionsSupportedByGraalvm = jdkVersionsSupportedByGraalvm;
    }

    /**
     * Gets the package name.
     *
     * @return the package name
     */
    @Override
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the package name.
     *
     * @param packageName the package name to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets the license path.
     *
     * @return the license path
     */
    @Override
    public String getLicensePath() {
        return licensePath;
    }

    /**
     * Sets the license path.
     *
     * @param licensePath the license path to set
     */
    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    /**
     * Gets the home page URL.
     *
     * @return the home page URL
     */
    @Override
    public String getHomePageUrl() {
        return homePageUrl;
    }

    /**
     * Sets the home page URL.
     *
     * @param homePageUrl the home page URL to set
     */
    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the default application name.
     *
     * @return the default application name
     */
    @Override
    public String getDefaultAppName() {
        return DEFAULT_APP_NAME;
    }

    /**
     * Gets the project generator URL.
     *
     * @return the project generator URL
     */
    @Override
    public String getProjectGeneratorUrl() {
        return LAUNCHER_URL;
    }

    /**
     * Gets the list of source file extensions with headers.
     *
     * @return the list of source file extensions
     */
    @Override
    public List<String> getFilesWithHeader() {
        return sourceFilesExtensions;
    }

    /**
     * Sets the list of source file extensions with headers.
     *
     * @param sourceFilesExtensions the list of source file extensions to set
     */
    public void setFilesWithHeader(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }

    /**
     * Gets the default minimum JDK version.
     *
     * @return the default minimum JDK version
     */
    @Override
    public int getDefaultMinJdk() {
        return DEFAULT_MIN_JDK;
    }

    /**
     * Gets the API URL.
     *
     * @return the API URL
     */
    @Override
    public String getApiUrl() {
        return API_URL;
    }

    /**
     * Gets the version path.
     *
     * @return the version path
     */
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version path.
     *
     * @param version the version path to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the environment JDK version.
     *
     * @return the environment JDK version
     */
    @Override
    public String getEnvJdkVersion() {
        return envJdkVersion;
    }

    /**
     * Sets the environment JDK version.
     *
     * @param envJdkVersion the environment JDK version to set
     */
    public void setEnvJdkVersion(String envJdkVersion) {
        this.envJdkVersion = envJdkVersion;
    }

    /**
     * Gets the default JDK version.
     *
     * @return the default JDK version
     */
    @Override
    public JdkVersion getDefaultJdkVersion() {
        return defaulJdkVersion;
    }

    /**
     * Gets the GitHub workflow Java CI.
     *
     * @return the GitHub workflow Java CI
     */
    @Override
    public String getGithubWorkflowJavaCi() {
        return githubWorkflowJavaCi;
    }

    /**
     * Sets the GitHub workflow Java CI.
     *
     * @param githubWorkflowJavaCi the GitHub workflow Java CI to set
     */
    public void setGithubWorkflowJavaCi(String githubWorkflowJavaCi) {
        this.githubWorkflowJavaCi = githubWorkflowJavaCi;
    }

    /**
     * Gets the environment GitHub workflow.
     *
     * @return the environment GitHub workflow
     */
    @Override
    public String getEnvGithubWorkflow() {
        return envGithubWorkflow;
    }

    /**
     * Sets the environment GitHub workflow.
     *
     * @param envGithubWorkflow the environment GitHub workflow to set
     */
    public void setEnvGithubWorkflow(String envGithubWorkflow) {
        this.envGithubWorkflow = envGithubWorkflow;
    }

    /**
     * Gets the system property for Micronaut guide.
     *
     * @return the system property for Micronaut guide
     */
    @Override
    public String getSysPropMicronautGuide() {
        return sysPropMicronautGuide;
    }

    /**
     * Sets the system property for Micronaut guide.
     *
     * @param sysPropMicronautGuide the system property for Micronaut guide to set
     */
    public void setSysPropMicronautGuide(String sysPropMicronautGuide) {
        this.sysPropMicronautGuide = sysPropMicronautGuide;
    }

    /**
     * Sets the default JDK version.
     *
     * @param jdkVersion the default JDK version to set
     */
    public void setDefaulJdkVersion(JdkVersion jdkVersion) {
        this.defaulJdkVersion = jdkVersion;
    }

    /**
     * Sets the list of source file extensions.
     *
     * @param sourceFilesExtensions the list of source file extensions to set
     */
    public void setSourceFilesExtensions(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }

    /**
     * Gets the guides directory.
     *
     * @return the guides directory
     */
    @Override
    public String getGuidesDir() {
        return guidesDir;
    }

    /**
     * Sets the guides directory.
     *
     * @param guidesDir the guides directory to set
     */
    public void setGuidesDir(String guidesDir) {
        this.guidesDir = guidesDir;
    }
}

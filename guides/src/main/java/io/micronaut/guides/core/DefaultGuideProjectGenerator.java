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

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.JdkVersion.JDK_8;

/**
 * Builder class for constructing SourceBlock instances.
 */
@Singleton
public class DefaultGuideProjectGenerator implements GuideProjectGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideProjectGenerator.class);
    private final GuidesConfiguration guidesConfiguration;
    private final ProjectGenerator projectGenerator;

    DefaultGuideProjectGenerator(GuidesConfiguration guidesConfiguration, ProjectGenerator projectGenerator) {
        this.guidesConfiguration = guidesConfiguration;
        this.projectGenerator = projectGenerator;
    }

    /**
     * Generates the project files for the given guide in the specified output directory.
     *
     * @param outputDirectory the directory where the project files will be generated
     * @param guide           the guide containing the project details
     * @throws IOException if an I/O error occurs during project generation
     */
    @Override
    public void generate(@NotNull @NonNull File outputDirectory, @NotNull @NonNull Guide guide) throws IOException {
        if (!outputDirectory.exists()) {
            throw new ConfigurationException("Output directory does not exist");
        }

        if (!outputDirectory.isDirectory()) {
            throw new ConfigurationException("Output directory must be a directory");
        }

        JdkVersion javaVersion = GuideGenerationUtils.resolveJdkVersion(guidesConfiguration, guide);
        if (GuideGenerationUtils.skipBecauseOfJavaVersion(guide, guidesConfiguration)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("not generating project for {}, JDK {} > {}", guide.getSlug(), javaVersion.majorVersion(), guide.getMaximumJavaVersion());
            }
            return;
        }

        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide, LOG);
        for (GuidesOption guidesOption : guidesOptionList) {
            generate(outputDirectory, guide, guidesOption, javaVersion);
        }
    }

    /**
     * Generates the project files for the given guide and guides option in the specified output directory.
     *
     * @param outputDirectory the directory where the project files will be generated
     * @param guide           the guide containing the project details
     * @param guidesOption    the guides option containing additional configuration
     * @param javaVersion     the JDK version to be used for the project
     * @throws IOException if an I/O error occurs during project generation
     */
    public void generate(@NonNull File outputDirectory, @NonNull Guide guide, @NonNull GuidesOption guidesOption, @NonNull JdkVersion javaVersion) throws IOException {
        for (App app : guide.getApps()) {
            generate(outputDirectory, guide, guidesOption, javaVersion, app);
        }
    }

    /**
     * Generates the project files for the given guide, guides option, and app in the specified output directory.
     *
     * @param outputDirectory the directory where the project files will be generated
     * @param guide           the guide containing the project details
     * @param guidesOption    the guides option containing additional configuration
     * @param javaVersion     the JDK version to be used for the project
     * @param app             the app containing the application details
     * @throws IOException if an I/O error occurs during project generation
     */
    public void generate(@NonNull File outputDirectory, @NonNull Guide guide, @NonNull GuidesOption guidesOption, @NonNull JdkVersion javaVersion, @NonNull App app) throws IOException {
        List<String> appFeatures = new ArrayList<>(app.features(guidesOption.getLanguage()));
        if (!guidesConfiguration.getJdkVersionsSupportedByGraalvm().contains(javaVersion)) {
            appFeatures.remove("graalvm");
        }

        // typical guides use 'default' as name, multi-project guides have different modules
        String folder = MacroUtils.getSourceDir(guide.getSlug(), guidesOption);

        String appName = app.getName().equals(guidesConfiguration.getDefaultAppName()) ? EMPTY_STRING : app.getName();

        Path destinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName);
        File destination = destinationPath.toFile();
        destination.mkdir();

        String packageAndName = guidesConfiguration.getPackageName() + '.' + app.getName();
        GeneratorContext generatorContext = createProjectGeneratorContext(app.getApplicationType(),
                packageAndName,
                app.getFramework(),
                appFeatures,
                guidesOption.getBuildTool(),
                app.getTestFramework() != null ? app.getTestFramework() : guidesOption.getTestFramework(),
                guidesOption.getLanguage(),
                javaVersion);
        try {
            projectGenerator.generate(app.getApplicationType(),
                    generatorContext.getProject(),
                    new FileSystemOutputHandler(destination, ConsoleOutput.NOOP),
                    generatorContext);
        } catch (Exception e) {
            LOG.error("Error generating application: " + e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

    private GeneratorContext createProjectGeneratorContext(ApplicationType type, @Pattern(regexp = "[\\w\\d-_\\.]+") String packageAndName, @Nullable String framework, @Nullable List<String> features, @Nullable BuildTool buildTool, @Nullable TestFramework testFramework, @Nullable Language lang, @Nullable JdkVersion javaVersion) throws IllegalArgumentException {
        Project project;
        try {
            project = NameUtils.parse(packageAndName);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(BAD_REQUEST, "Invalid project name: " + e.getMessage());
        }

        return projectGenerator.createGeneratorContext(
                type,
                project,
                new Options(
                        lang,
                        testFramework != null ? testFramework.toTestFramework() : null,
                        buildTool == null ? GRADLE : buildTool,
                        javaVersion != null ? javaVersion : JDK_8).withFramework(framework),
                null,
                features != null ? features : Collections.emptyList(),
                ConsoleOutput.NOOP
        );
    }
}

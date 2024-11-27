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
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.api.TestFramework.JUNIT;
import static io.micronaut.starter.api.TestFramework.SPOCK;
import static io.micronaut.starter.options.Language.GROOVY;

/**
 * Utility class for generating paths and options for guides.
 * This class provides methods to generate paths for main and test files,
 * resolve JDK versions, and process guide metadata.
 */
public final class GuideGenerationUtils {

    private GuideGenerationUtils() {
    }

    /**
     * Generates the main path for a given application name, file name, option, and configuration.
     *
     * @param appName       the name of the application
     * @param fileName      the name of the file
     * @param option        the GuidesOption to consider
     * @param configuration the GuidesConfiguration to use
     * @return the generated main path
     */
    @NonNull
    static String mainPath(@NonNull String appName,
                           @NonNull String fileName,
                           @NonNull GuidesOption option,
                           @NonNull GuidesConfiguration configuration) {
        return pathByFolder(appName, fileName, "main", option, configuration);
    }

    /**
     * Generates the test path for a given application name, file name, option, and configuration.
     *
     * @param appName       the name of the application
     * @param name          the name of the file
     * @param option        the GuidesOption to consider
     * @param configuration the GuidesConfiguration to use
     * @return the generated test path
     */
    @NonNull
    static String testPath(@NonNull String appName,
                           @NonNull String name,
                           @NonNull GuidesOption option,
                           @NonNull GuidesConfiguration configuration) {
        String fileName = name;
        if (option.getTestFramework() != null) {
            if (name.endsWith("Test")) {
                fileName = name.substring(0, name.indexOf("Test"));
                fileName += option.getTestFramework() == SPOCK ? "Spec" : "Test";
            }
        }

        return pathByFolder(appName, fileName, "test", option, configuration);
    }

    /**
     * Generates a path by folder for a given application name, file name, folder, option, and configuration.
     *
     * @param appName       the name of the application
     * @param fileName      the name of the file
     * @param folder        the folder name (e.g., "main" or "test")
     * @param option        the GuidesOption to consider
     * @param configuration the GuidesConfiguration to use
     * @return the generated path
     */
    @NonNull
    static String pathByFolder(@NonNull String appName,
                               @NonNull String fileName,
                               @NonNull String folder,
                               @NonNull GuidesOption option,
                               @NonNull GuidesConfiguration configuration) {
        String module = StringUtils.isNotEmpty(appName) ? appName + "/" : "";
        Path path = Path.of(module,
                "src",
                folder,
                option.getLanguage().getName(),
                configuration.getPackageName().replace(".", "/"),
                fileName + "." + option.getLanguage().getExtension());
        return path.toString();
    }

    /**
     * Generates a list of GuidesOption based on the given guide metadata and logger.
     *
     * @param guideMetadata the guide metadata to consider
     * @param logger        the logger to use
     * @return the list of generated GuidesOption
     */
    @NonNull
    public static List<GuidesOption> guidesOptions(@NonNull Guide guideMetadata,
                                                   @NonNull Logger logger) {
        List<BuildTool> buildTools = guideMetadata.getBuildTools();
        List<Language> languages = guideMetadata.getLanguages();
        TestFramework testFramework = guideMetadata.getTestFramework();
        List<GuidesOption> guidesOptionList = new ArrayList<>();

        for (BuildTool buildTool : buildTools) {
            for (Language language : Language.values()) {
                if (guideMetadata.shouldSkip(buildTool)) {
                    logger.info("Skipping index guide for {} and {}", buildTool, language);
                    continue;
                }
                if (languages.contains(language)) {
                    guidesOptionList.add(new GuidesOption(buildTool, language, testFrameworkOption(language, testFramework)));
                }
            }
        }

        return guidesOptionList;
    }

    /**
     * Determines the test framework option based on the given language and test framework.
     *
     * @param language      the language to consider
     * @param testFramework the test framework to consider
     * @return the determined test framework option
     */
    @NonNull
    static TestFramework testFrameworkOption(@NonNull Language language,
                                             @Nullable TestFramework testFramework) {
        if (testFramework != null) {
            return testFramework;
        }
        if (language == GROOVY) {
            return SPOCK;
        }
        return JUNIT;
    }

    /**
     * Resolves the JDK version based on the given guides configuration.
     *
     * @param guidesConfiguration the guides configuration to use
     * @return the resolved JDK version
     */
    @NonNull
    static JdkVersion resolveJdkVersion(@NonNull GuidesConfiguration guidesConfiguration) {
        JdkVersion javaVersion;
        if (System.getenv(guidesConfiguration.getEnvJdkVersion()) != null) {
            try {
                int majorVersion = Integer.parseInt(System.getenv(guidesConfiguration.getEnvJdkVersion()));
                javaVersion = JdkVersion.valueOf(majorVersion);
            } catch (NumberFormatException ignored) {
                throw new ConfigurationException("Could not parse env " + guidesConfiguration.getEnvJdkVersion() + " to JdkVersion");
            }
        } else {
            try {
                javaVersion = JdkVersion.JDK_21;
                //TODO javaVersion = JdkVersion.valueOf(Integer.parseInt(JavaVersion.current().getMajorVersion()));
            } catch (IllegalArgumentException ex) {
                System.out.println("WARNING: " + ex.getMessage() + ": Defaulting to " + guidesConfiguration.getDefaultJdkVersion());
                javaVersion = guidesConfiguration.getDefaultJdkVersion();
            }
        }
        return javaVersion;
    }

    /**
     * Resolves the JDK version based on the given guides configuration and guide.
     *
     * @param guidesConfiguration the guides configuration to use
     * @param guide               the guide to consider
     * @return the resolved JDK version
     */
    public static JdkVersion resolveJdkVersion(GuidesConfiguration guidesConfiguration, @NotNull @NonNull Guide guide) {
        JdkVersion javaVersion = GuideGenerationUtils.resolveJdkVersion(guidesConfiguration);
        if (guide.getMinimumJavaVersion() != null) {
            JdkVersion minimumJavaVersion = JdkVersion.valueOf(guide.getMinimumJavaVersion());
            if (minimumJavaVersion.majorVersion() > javaVersion.majorVersion()) {
                return minimumJavaVersion;
            }
        }
        return javaVersion;
    }

    /**
     * Determines whether to skip processing a guide based on the Java version.
     *
     * @param metadata            the guide metadata to consider
     * @param guidesConfiguration the guides configuration to use
     * @return true if the guide should be skipped, false otherwise
     */
    static boolean skipBecauseOfJavaVersion(Guide metadata, GuidesConfiguration guidesConfiguration) {
        int jdkVersion = resolveJdkVersion(guidesConfiguration).majorVersion();
        return (metadata.getMinimumJavaVersion() != null && jdkVersion < metadata.getMinimumJavaVersion()) ||
                (metadata.getMaximumJavaVersion() != null && jdkVersion > metadata.getMaximumJavaVersion());
    }

    /**
     * Retrieves the single guide property from the system properties.
     *
     * @param guidesConfiguration the guides configuration to use
     * @return the single guide property
     */
    public static String singleGuide(GuidesConfiguration guidesConfiguration) {
        return System.getProperty(guidesConfiguration.getSysPropMicronautGuide());
    }

    /**
     * Checks whether a guide should be processed a guide based on the given metadata, JDK check flag, and guides configuration.
     *
     * @param metadata            the guide metadata to consider
     * @param checkJdk            whether to check the JDK version
     * @param guidesConfiguration the guides configuration to use
     * @return true if the guide should be processed, false otherwise
     */
    public static boolean process(Guide metadata, boolean checkJdk, GuidesConfiguration guidesConfiguration) {

        if (!metadata.isPublish()) {
            return false;
        }

        boolean processGuide = singleGuide(guidesConfiguration) == null || singleGuide(guidesConfiguration).equals(metadata.getSlug());
        if (!processGuide) {
            return false;
        }

        if (checkJdk && skipBecauseOfJavaVersion(metadata, guidesConfiguration)) {
            System.out.println("Not processing " + metadata.getSlug() + ", JDK not between " +
                    metadata.getMinimumJavaVersion() + " and " + metadata.getMaximumJavaVersion());
            return false;
        }

        return true;
    }
}

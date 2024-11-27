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

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;

/**
 * DefaultTestScriptGenerator is a singleton class that implements the TestScriptGenerator interface.
 * It provides methods to generate test scripts for guides.
 */
@Singleton
@Internal
class DefaultTestScriptGenerator implements TestScriptGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTestScriptGenerator.class);

    private final GuidesConfiguration guidesConfiguration;
    private final GuideParser guideParser;

    DefaultTestScriptGenerator(GuidesConfiguration guidesConfiguration, GuideParser guideParser) {
        this.guidesConfiguration = guidesConfiguration;
        this.guideParser = guideParser;
    }

    private static List<String> guidesChanged(List<String> changedFiles) {
        return changedFiles.stream()
                .filter(path -> path.startsWith("guides"))
                .map(path -> {
                    String guideFolder = path.substring("guides/".length());
                    return guideFolder.substring(0, guideFolder.indexOf('/'));
                })
                .distinct()
                .collect(Collectors.toList());
    }

    private static boolean changesMicronautVersion(List<String> changedFiles) {
        return changedFiles.stream().anyMatch(file -> file.contains("version.txt"));
    }

    private static boolean changesDependencies(List<String> changedFiles, List<String> changedGuides) {
        if (!changedGuides.isEmpty()) {
            return false;
        }
        return changedFiles.stream().anyMatch(file -> file.contains("pom.xml"));
    }

    private static boolean changesBuildScr(List<String> changedFiles) {
        return changedFiles.stream().anyMatch(file -> file.contains("buildSrc"));
    }

    private static String scriptForFolder(String nestedFolder,
                                          String folder,
                                          boolean stopIfFailure,
                                          BuildTool buildTool,
                                          boolean noDaemon,
                                          boolean nativeTest,
                                          boolean validateLicense) {
        String testCopy = nativeTest ? "native tests" : "tests";
        StringBuilder bashScript = new StringBuilder(String.format(
                """
                        cd %s
                        echo "-------------------------------------------------"
                        echo "Executing '%s' %s"
                        """,
                nestedFolder, folder, testCopy
        ));

        if (noDaemon) {
            bashScript.append("kill_kotlin_daemon\n");
        }

        if (nativeTest) {
            bashScript.append(String.format(
                    "%s || EXIT_STATUS=$?\n",
                    buildTool == BuildTool.MAVEN ? "./mvnw -Pnative test" : "./gradlew nativeTest"
            ));
        } else {
            String mavenCommand = validateLicense ? "./mvnw -q test spotless:check" : "./mvnw -q test";
            bashScript.append(String.format(
                    """
                            %s || EXIT_STATUS=$?
                            echo "Stopping shared test resources service (if created)"
                            %s > /dev/null 2>&1 || true
                            """,
                    buildTool == BuildTool.MAVEN ? mavenCommand : "./gradlew -q check",
                    buildTool == BuildTool.MAVEN ? "./mvnw -q mn:stop-testresources-service" : "./gradlew -q stopTestResourcesService"
            ));
        }

        if (noDaemon) {
            bashScript.append("kill_kotlin_daemon\n");
        }

        bashScript.append("cd ..\n");

        if (stopIfFailure) {
            bashScript.append(String.format(
                    """
                            if [ $EXIT_STATUS -ne 0 ]; then
                              echo "'%s' %s failed => exit $EXIT_STATUS"
                              exit $EXIT_STATUS
                            fi
                            """,
                    folder, testCopy
            ));
        } else {
            bashScript.append(String.format(
                    """
                            if [ $EXIT_STATUS -ne 0 ]; then
                              FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" %s)
                              echo "'%s' %s failed => exit $EXIT_STATUS"
                            fi
                            EXIT_STATUS=0
                            """,
                    folder, folder, testCopy
            ));
        }

        return bashScript.toString();
    }

    private static boolean shouldSkip(Guide metadata,
                                      List<String> guidesChanged,
                                      boolean forceExecuteEveryTest,
                                      GuidesConfiguration guidesConfiguration) {

        if (!GuideGenerationUtils.process(metadata, false, guidesConfiguration)) {
            return true;
        }

        if (forceExecuteEveryTest) {
            return false;
        }

        return !guidesChanged.contains(metadata.getSlug());
    }

    /**
     * Checks if the given app supports native tests.
     *
     * @param app          the app to check
     * @param guidesOption the guides option containing additional configuration
     * @return true if the app supports native tests, false otherwise
     */
    @Override
    public boolean supportsNativeTest(App app, GuidesOption guidesOption) {
        return isMicronautFramework(app) &&
                guidesOption.getBuildTool() == GRADLE &&
                supportsNativeTest(guidesOption.getLanguage()) &&
                guidesOption.getTestFramework() == TestFramework.JUNIT;
    }

    /**
     * Checks if the given app uses the Micronaut framework.
     *
     * @param app the app to check
     * @return true if the app uses the Micronaut framework, false otherwise
     */
    @Override
    public boolean isMicronautFramework(App app) {
        return app.getFramework() == null || app.getFramework().equals("Micronaut");
    }

    /**
     * Checks if the given language supports native tests.
     *
     * @param language the language to check
     * @return true if the language supports native tests, false otherwise
     */
    @Override
    public boolean supportsNativeTest(Language language) {
        return language != Language.GROOVY;
    }

    /**
     * Generates a script for running native tests for the given guides metadata.
     *
     * @param metadatas the list of guides metadata
     * @return the generated script as a string
     */
    @Override
    public String generateNativeTestScript(@NonNull @NotNull List<? extends Guide> metadatas) {
        return generateScript(metadatas, false, true);
    }

    /**
     * Generates a script for running tests for the given guides metadata.
     *
     * @param metadatas the list of guides metadata
     * @return the generated script as a string
     */
    @Override
    public String generateTestScript(@NonNull @NotNull List<? extends Guide> metadatas) {
        return generateScript(metadatas, false, false);
    }

    /**
     * Generates a script for running tests based on the changed files.
     *
     * @param guidesFolder       the folder containing the guides
     * @param metadataConfigName the name of the metadata configuration
     * @param stopIfFailure      whether to stop if a test fails
     * @param changedFiles       the list of changed files
     * @return the generated script as a string
     */
    public String generateScript(File guidesFolder,
                                 String metadataConfigName,
                                 boolean stopIfFailure,
                                 List<String> changedFiles) {
        List<String> slugsChanged = guidesChanged(changedFiles);
        boolean forceExecuteEveryTest = changesMicronautVersion(changedFiles) ||
                changesDependencies(changedFiles, slugsChanged) ||
                changesBuildScr(changedFiles) ||
                (System.getenv(guidesConfiguration.getEnvGithubWorkflow()) != null &&
                        !System.getenv(guidesConfiguration.getEnvGithubWorkflow()).equals(guidesConfiguration.getGithubWorkflowJavaCi())) ||
                (changedFiles.isEmpty() && System.getenv(guidesConfiguration.getEnvGithubWorkflow()) == null);

        List<? extends Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName);
        metadatas = metadatas.stream()
                .filter(metadata -> !shouldSkip(metadata, slugsChanged, forceExecuteEveryTest, guidesConfiguration))
                .collect(Collectors.toList());
        return generateScript(metadatas, stopIfFailure, false);
    }

    /**
     * Generates a script for running tests for the given guides metadata.
     *
     * @param metadatas     the list of guides metadata
     * @param stopIfFailure whether to stop if a test fails
     * @param nativeTest    whether to run native tests
     * @return the generated script as a string
     */
    public String generateScript(List<? extends Guide> metadatas, boolean stopIfFailure, boolean nativeTest) {
        StringBuilder bashScript = new StringBuilder("""
                #!/usr/bin/env bash
                set -e
                
                FAILED_PROJECTS=()
                EXIT_STATUS=0
                
                kill_kotlin_daemon () {
                  echo "Killing KotlinCompile daemon to pick up fresh properties (due to kapt and java > 17)"
                  for daemon in $(jps | grep KotlinCompile | cut -d' ' -f1); do
                    echo "Killing $daemon"
                    kill -9 $daemon
                  done
                }""");

        metadatas.sort(Comparator.comparing(Guide::getSlug));
        for (Guide metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(metadata, LOG);
            bashScript.append("\n");
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = MacroUtils.getSourceDir(metadata.getSlug(), guidesOption);
                BuildTool buildTool = folder.contains(MAVEN.toString()) ? MAVEN : GRADLE;
                if (metadata.getApps().stream().anyMatch(app -> app.getName().equals(guidesConfiguration.getDefaultAppName()))) {
                    if (metadata.shouldSkip(buildTool)) {
                        continue;
                    }
                    Optional<? extends App> appOptional = metadata.getApps().stream().filter(app -> app.getName().equals(guidesConfiguration.getDefaultAppName())).findFirst();
                    if (appOptional.isPresent()) {
                        App defaultApp = appOptional.get();
                        if (!nativeTest || supportsNativeTest(defaultApp, guidesOption)) {
                            List<String> features = defaultApp.features(guidesOption.getLanguage());
                            if (!folder.contains("-maven-groovy")) {
                                bashScript.append(scriptForFolder(folder, folder, stopIfFailure, buildTool, features.contains("kapt") && Runtime.getRuntime().version().feature() > 17 && buildTool == GRADLE, nativeTest, defaultApp.isValidateLicense()));
                            }
                        }
                    }
                } else {
                    bashScript.append("cd " + folder + "\n");
                    for (App app : metadata.getApps()) {
                        if (metadata.shouldSkip(buildTool)) {
                            continue;
                        }
                        if (!nativeTest || supportsNativeTest(app, guidesOption)) {
                            List<String> features = app.features(guidesOption.getLanguage());
                            if (!folder.contains("-maven-groovy")) {
                                bashScript.append(scriptForFolder(app.getName(), folder + "/" + app.getName(), stopIfFailure, buildTool, features.contains("kapt") && Runtime.getRuntime().version().feature() > 17 && buildTool == GRADLE, nativeTest, app.isValidateLicense()));
                            }
                        }
                    }
                    bashScript.append("\ncd ..\n");
                }
            }
        }

        if (!stopIfFailure) {
            bashScript.append("""
                    if [ ${#FAILED_PROJECTS[@]} -ne 0 ]; then
                      echo ""
                      echo "-------------------------------------------------"
                      echo "Projects with errors:"
                      for p in `echo ${FAILED_PROJECTS[@]}`; do
                        echo "  $p"
                      done;
                      echo "-------------------------------------------------"
                      exit 1
                    else
                      exit 0
                    fi
                    
                    """);
        }

        return bashScript.toString();
    }
}

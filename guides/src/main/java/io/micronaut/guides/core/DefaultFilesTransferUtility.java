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
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;

/**
 * Class that provides utility methods for transferring files.
 */
@Internal
@Singleton
class DefaultFilesTransferUtility implements FilesTransferUtility {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFilesTransferUtility.class);
    private static final String EXTENSION_JAVA = ".java";
    private static final String EXTENSION_GROOVY = ".groovy";
    private static final String EXTENSION_KT = ".kt";

    private final LicenseLoader licenseLoader;
    private final GuidesConfiguration guidesConfiguration;

    /**
     * Constructs a new DefaultFilesTransferUtility.
     *
     * @param licenseLoader       the license loader
     * @param guidesConfiguration the guides configuration
     */
    DefaultFilesTransferUtility(LicenseLoader licenseLoader, GuidesConfiguration guidesConfiguration) {
        this.licenseLoader = licenseLoader;
        this.guidesConfiguration = guidesConfiguration;
    }

    /**
     * Checks if a file contains the specified text.
     *
     * @param file the file to check
     * @param text the text to look for
     * @return true if the file contains the text, false otherwise
     */
    private static boolean fileContainsText(File file, String text) {
        try {
            return new String(Files.readAllBytes(file.toPath())).contains(text);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies guide source files from the input directory to the destination path.
     *
     * @param inputDir                 the input directory
     * @param destinationPath          the destination path
     * @param appName                  the application name
     * @param language                 the programming language
     * @param ignoreMissingDirectories whether to ignore missing directories
     * @throws IOException if an I/O error occurs during file copy
     */
    private static void copyGuideSourceFiles(File inputDir, Path destinationPath, String appName, String language, boolean ignoreMissingDirectories) throws IOException {

        // look for a common 'src' directory shared by multiple languages and copy those files first
        final String srcFolder = "src";
        Path srcPath = Paths.get(inputDir.getAbsolutePath(), appName, srcFolder);
        if (Files.exists(srcPath)) {
            Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder)));
        }

        Path sourcePath = Paths.get(inputDir.getAbsolutePath(), appName, language);
        if (!Files.exists(sourcePath)) {
            sourcePath.toFile().mkdir();
        }
        if (Files.exists(sourcePath)) {
            // copy source/resource files for the current language
            Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath));
        } else if (!ignoreMissingDirectories) {
            throw new ConfigurationException("source directory " + sourcePath.toFile().getAbsolutePath() + " does not exist");
        }
    }

    /**
     * Returns a file to be deleted based on the destination and path.
     *
     * @param destination the destination directory
     * @param path        the path of the file to delete
     * @return the file to delete
     */
    private static File fileToDelete(File destination, String path) {
        return Paths.get(destination.getAbsolutePath(), path).toFile();
    }

    /**
     * Copies a file from the input directory to the destination root.
     *
     * @param inputDir        the input directory
     * @param destinationRoot the destination root
     * @param filePath        the file path
     * @throws IOException if an I/O error occurs during file copy
     */
    private static void copyFile(File inputDir, File destinationRoot, String filePath) throws IOException {
        File sourceFile = new File(inputDir, filePath);
        File destinationFile = new File(destinationRoot, filePath);

        File destinationFileDir = destinationFile.getParentFile();
        if (!destinationFileDir.exists()) {
            Files.createDirectories(destinationFileDir.toPath());
        }

        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Transfers files from the input directory to the output directory for the provided guide.
     *
     * @param inputDirectory  the directory containing the input files
     * @param outputDirectory the directory where the files will be transferred
     * @param guide           the guide metadata
     * @throws IOException if an I/O error occurs during file transfer
     */
    @Override
    public void transferFiles(@NotNull @NonNull File inputDirectory, @NotNull @NonNull File outputDirectory, @NotNull @NonNull Guide guide) throws IOException {
        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide, LOG);
        for (GuidesOption guidesOption : guidesOptionList) {
            for (App app : guide.getApps()) {
                String appName = app.getName().equals(guidesConfiguration.getDefaultAppName()) ? EMPTY_STRING : app.getName();
                String folder = MacroUtils.getSourceDir(guide.getSlug(), guidesOption);
                Path destinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName);
                File destination = destinationPath.toFile();

                if (guide.getBase() != null) {
                    File baseDir = new File(inputDirectory.getParentFile(), guide.getBase());
                    copyGuideSourceFiles(baseDir, destinationPath, appName, guidesOption.getLanguage().toString(), true);
                }

                copyGuideSourceFiles(inputDirectory, destinationPath, appName, guidesOption.getLanguage().toString(), false);

                if (app.getExcludeSource() != null) {
                    for (String mainSource : app.getExcludeSource()) {
                        File f = fileToDelete(destination, GuideGenerationUtils.mainPath(appName, mainSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                        f = fileToDelete(destination, GuideGenerationUtils.mainPath(appName, mainSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }

                if (app.getExcludeTest() != null) {
                    for (String testSource : app.getExcludeTest()) {
                        File f = fileToDelete(destination, GuideGenerationUtils.testPath(appName, testSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                        f = fileToDelete(destination, GuideGenerationUtils.testPath(appName, testSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }

                if (guide.getZipIncludes() != null) {
                    File destinationRoot = new File(outputDirectory.getAbsolutePath(), folder);
                    for (String zipInclude : guide.getZipIncludes()) {
                        copyFile(inputDirectory, destinationRoot, zipInclude);
                    }
                }
                addLicenses(new File(outputDirectory.getAbsolutePath(), folder));
            }
        }
    }

    /**
     * Adds license headers to the files in the specified folder.
     *
     * @param folder the folder containing the files to which license headers will be added
     */
    void addLicenses(File folder) {
        String licenseHeader = licenseLoader.getLicenseHeaderText();
        Arrays.stream(folder.listFiles()).forEach(file -> {
            if ((file.getPath().endsWith(EXTENSION_JAVA) || file.getPath().endsWith(EXTENSION_GROOVY) || file.getPath().endsWith(EXTENSION_KT))
                    && !fileContainsText(file, "Licensed under")) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    Files.write(file.toPath(), (licenseHeader + content).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

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
import io.micronaut.core.util.ArrayUtils;
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
import java.util.*;

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

        if (Files.exists(sourcePath)) {
            // copy source/resource files for the current language
            Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath));
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
        copyFile(inputDir, destinationRoot, sourceFile);
    }

    /**
     * Copies a file from the input directory to the destination root.
     *
     * @param inputDir        the input directory
     * @param destinationRoot the destination root
     * @param sourceFile      sourceFile
     * @throws IOException if an I/O error occurs during file copy
     */
    private static void copyFile(File inputDir, File destinationRoot, File sourceFile) throws IOException {
        String filePath = sourceFile.getAbsolutePath().substring(inputDir.getAbsolutePath().length());
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
    public void transferFiles(@NotNull @NonNull File inputDirectory,
                              @NotNull @NonNull File outputDirectory,
                              @NotNull @NonNull Guide guide,
                              @NotNull @NonNull List<? extends Guide> guides) throws IOException {
        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide, LOG);
        for (GuidesOption guidesOption : guidesOptionList) {
            for (App app : guide.getApps()) {
                String appName = guide.getApps().size() > 1 ? app.getName() : EMPTY_STRING;
                String folder = MacroUtils.getSourceDir(guide.getSlug(), guidesOption);
                String module = guide.getSourceModule() != null ? guide.getSourceModule() : "";
                Path destinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName, module);
                File destination = destinationPath.toFile();

                if (guide.getBase() != null) {
                    guides.stream()
                            .filter(g -> g.getSlug().equals(guide.getBase()))
                            .findFirst()
                            .ifPresent(parentGuide -> {
                                File baseDir = parentGuide.getFolder();
                                String baseModule = guide.getBaseSourceModule() != null ? guide.getBaseSourceModule() : module;
                                Path baseDestinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName, baseModule);
                                try {
                                    copyGuideSourceFiles(baseDir, baseDestinationPath, appName, guidesOption.getLanguage().toString(), true);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                }

                copyGuideSourceFiles(inputDirectory, destinationPath, appName, guidesOption.getLanguage().toString(), false);

                if (app.getExcludeSource() != null) {
                    deleteFiles(app.getExcludeSource(), destination, app, guidesOption, guidesConfiguration, "main");
                }

                if (app.getExcludeBaseSource() != null) {
                    String baseModule = guide.getBaseSourceModule() != null ? guide.getBaseSourceModule() : module;
                    Path baseDestinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName, baseModule);
                    deleteFiles(app.getExcludeBaseSource(), baseDestinationPath.toFile(), app, guidesOption, guidesConfiguration, "main");
                }

                if (app.getExcludeTest() != null) {
                    deleteFiles(app.getExcludeTest(), destination, app, guidesOption, guidesConfiguration, "test");
                }

                if (app.getExcludeBaseTest() != null) {
                    String baseModule = guide.getBaseSourceModule() != null ? guide.getBaseSourceModule() : module;
                    Path baseDestinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName, baseModule);
                    deleteFiles(app.getExcludeBaseTest(), baseDestinationPath.toFile(), app, guidesOption, guidesConfiguration, "test");
                }


                File destinationRoot = new File(outputDirectory.getAbsolutePath(), folder);
                if (guide.getZipIncludes() != null) {
                    for (String zipInclude : guide.getZipIncludes()) {
                        copyFile(inputDirectory, destinationRoot, zipInclude);
                    }
                }
                for (File f : walkZipIncludeExtensions(inputDirectory)) {
                    copyFile(inputDirectory, destinationRoot, f);
                }
                addLicenses(new File(outputDirectory.getAbsolutePath(), folder));
            }
        }
    }

    private void deleteFiles(List<String> sources, File destination, App app, GuidesOption guidesOption, GuidesConfiguration guidesConfiguration, String pathType) {
        if (sources.size() == 1 && sources.get(0).equals("*")) {
            File destinationFolder = new File(destination, "src/" + pathType);
            //delete all files in the destination folder and its subfolders
            if (destinationFolder.exists()) {
                Arrays.stream(destinationFolder.listFiles()).forEach(file -> {
                    if (file.isDirectory()) {
                        try {
                            Files.walk(file.toPath())
                                    .sorted(Comparator.reverseOrder())
                                    .map(Path::toFile)
                                    .forEach(File::delete);
                        } catch (IOException e) {
                            LOG.warn("Failed to delete directory {}", file, e);
                        }
                    } else {
                        file.delete();
                    }
                });
            }

            return;
        }

        for (String source : sources) {
            String path = pathByFolder(app, source, pathType, guidesOption);
            File file = fileToDelete(destination, path);
            if (file.exists()) {
                file.delete();
            } else {
                LOG.warn("File {} to delete does not exist", file);
            }
        }
    }

    private List<File> walkZipIncludeExtensions(File dir) {
        List<File> result = new ArrayList<>();
        File[] list = dir.listFiles();
        if (ArrayUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        for (File f : list) {
            if (f.isDirectory()) {
                result.addAll(walkZipIncludeExtensions(f));
            } else if (guidesConfiguration.getZipIncludesExtensions().stream().anyMatch(ext -> f.getName().endsWith(ext))) {
                result.add(f);
            }
        }
        return result;
    }

    /**
     * Generates a path by folder for a given application name, file name, folder, option, and configuration.
     *
     * @param app      the application
     * @param fileName the name of the file
     * @param folder   the folder name (e.g., "main" or "test")
     * @param option   the GuidesOption to consider
     * @return the generated path
     */
    @NonNull
    static String pathByFolder(@NonNull App app,
                               @NonNull String fileName,
                               @NonNull String folder,
                               @NonNull GuidesOption option) {
        Path path = Path.of(
                "src",
                folder,
                option.getLanguage().getName(),
                app.getPackageName().replace(".", "/"),
                fileName + "." + option.getLanguage().getExtension());
        return path.toString();
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

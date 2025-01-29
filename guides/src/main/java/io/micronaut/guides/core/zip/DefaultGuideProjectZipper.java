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
package io.micronaut.guides.core.zip;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideGenerationUtils;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.guides.core.MacroUtils;
import jakarta.inject.Singleton;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Default implementation of the {@link GuideProjectZipper} interface.
 * This class provides functionality to zip a directory into a single output file.
 */
@Singleton
public class DefaultGuideProjectZipper implements GuideProjectZipper {
    private static final List<String> EXCLUDED_FILES = List.of(".idea", ".DS_Store");
    private static final List<String> EXECUTABLES = List.of("gradlew", "gradlew.bat", "mvnw", "mvnw.bat");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideProjectZipper.class);

    /**
     * Compresses the contents of the specified source directory into a zip file.
     *
     * @param rootDir   the root directory of the source files
     * @param sourceDir the source directory to be compressed
     * @param out       the {@link ZipArchiveOutputStream} to write the compressed data
     * @throws IOException if an I/O error occurs during compression
     */
    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipArchiveOutputStream out) throws IOException {
        for (File file : new File(sourceDir).listFiles()) {
            if (EXCLUDED_FILES.contains(file.getName())) {
                continue;
            }
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + File.separatorChar + file.getName(), out);
            } else {
                String zipPath = sourceDir.replace(rootDir, "") + '/' + file.getName();
                if (zipPath.charAt(0) == '/') {
                    zipPath = zipPath.substring(1);
                }
                ZipArchiveEntry entry = new ZipArchiveEntry(zipPath);
                if (EXECUTABLES.contains(file.getName())) {
                    entry.setUnixMode(UnixStat.FILE_FLAG | 0755);
                }
                out.putArchiveEntry(entry);
                Path p = Paths.get(sourceDir, file.getName());
                FileInputStream in = new FileInputStream(p.toFile());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                out.closeArchiveEntry();
            }
        }
    }

    @Override
    public void zipGuide(Guide guide, File guideOutput, File outputDirectory) throws IOException {
        List<GuidesOption> guideOptions = GuideGenerationUtils.guidesOptions(guide, LOG);
        for (GuidesOption guidesOption : guideOptions) {
            String name = getZipFileName(guide, guidesOption);
            File zipFile = new File(outputDirectory, getZipFileName(guide, guidesOption) + ".zip");
            File folderFile = new File(guideOutput, name);
            zipDirectory(zipFile, folderFile);
        }
    }

    private void zipDirectory(File zipFile, File folderFile) throws IOException {
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(new FileOutputStream(zipFile));
        compressDirectoryToZipfile(folderFile.getAbsolutePath(), folderFile.getAbsolutePath(), zipOutputStream);
        IOUtils.closeQuietly(zipOutputStream);
    }

    @Override
    public String getZipFileName(Guide guide, GuidesOption options) {
        return MacroUtils.getSourceDir(guide.getSlug(), options);
    }
}

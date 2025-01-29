/*
 * Copyright 2017-2025 original authors
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FileGenerator {

    default void saveFile(String content, File outputDirectory, String filename) throws IOException {
        saveFile(content, outputDirectory, filename, false);
    }

    default void saveFile(String content, File outputDirectory, String filename, boolean executable) throws IOException {
        outputDirectory.mkdirs();
        Path filePath = Paths.get(outputDirectory.getAbsolutePath(), filename);
        Files.write(filePath, content.getBytes());
        if (executable) {
            filePath.toFile().setExecutable(true);
        }
    }
}

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

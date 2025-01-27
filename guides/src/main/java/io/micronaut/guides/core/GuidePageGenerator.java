package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

import java.io.File;
import java.io.IOException;

public interface GuidePageGenerator extends FileGenerator {
    void generatePage(@NonNull Guide guide, @NonNull File inputDirectory, @NonNull File outputDirectory, @NonNull File guideOutput) throws IOException;
}

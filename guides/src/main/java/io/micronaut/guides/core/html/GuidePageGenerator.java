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
package io.micronaut.guides.core.html;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.FileGenerator;
import io.micronaut.guides.core.Guide;

import java.io.File;
import java.io.IOException;

/**
 * Guide Page generator.
 */
@FunctionalInterface
public interface GuidePageGenerator extends FileGenerator {
    /**
     *
     * @param guide Guide
     * @param inputDirectory Input Directory
     * @param outputDirectory Output Directory
     * @param guideOutput Guide output
     * @throws IOException If an I/O error occurs reading from the file.
     */
    void generatePage(@NonNull Guide guide,
                      @NonNull File inputDirectory,
                      @NonNull File outputDirectory,
                      @NonNull File guideOutput) throws IOException;
}

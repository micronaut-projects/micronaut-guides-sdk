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

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * JsonFeedFileGenerator is an API to save a JSON Feed to an output directory.
 * from a list of Guide metadata objects.
 */
public interface JsonFeedFileGenerator extends FileGenerator {
    /**
     * Generate and save a JSON Feed in an output directory.
     *
     * @param metadatas       the list of Guide metadata objects
     * @param outputDirectory the directory to which the JSON feed should be written
     * @throws IOException if an I/O error occurs during the generation of the JSON feed
     */
    void saveJsonFeed(@NonNull @NotNull @NotEmpty List<? extends Guide> metadatas,
                      @NonNull @NotNull File outputDirectory) throws IOException;
}

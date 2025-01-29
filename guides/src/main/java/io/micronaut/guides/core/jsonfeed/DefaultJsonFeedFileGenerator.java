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
package io.micronaut.guides.core.jsonfeed;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.Guide;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Internal
@Singleton
class DefaultJsonFeedFileGenerator implements JsonFeedFileGenerator {
    private final JsonFeedConfiguration jsonFeedConfiguration;
    private final JsonFeedGenerator jsonFeedGenerator;

    DefaultJsonFeedFileGenerator(JsonFeedConfiguration jsonFeedConfiguration,
                                 JsonFeedGenerator jsonFeedGenerator) {
        this.jsonFeedConfiguration = jsonFeedConfiguration;
        this.jsonFeedGenerator = jsonFeedGenerator;
    }

    /**
     * Generates a JSON string representation of the JsonFeed from the provided list of guide metadata.
     *
     * @param metadatas the list of guide metadata
     * @param outputDirectory Output directory
     * @throws IOException if an I/O error occurs during JSON serialization
     */
    @Override
    public void saveJsonFeed(@NonNull @NotNull @NotEmpty List<? extends Guide> metadatas,
                             @NonNull @NotNull File outputDirectory) throws IOException {
        String json = jsonFeedGenerator.jsonFeedString(metadatas);
        try {
            saveFile(json, outputDirectory, jsonFeedConfiguration.getFilename());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

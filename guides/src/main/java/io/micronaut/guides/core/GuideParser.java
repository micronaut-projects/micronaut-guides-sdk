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
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * GuideParser is an interface that defines methods for parsing metadata of guides.
 * It provides methods to parse metadata for all guides in a specified directory
 * or for a single guide.
 */
public interface GuideParser {
    String DEFAULT_METADATA_NAME = "metadata.json";

    /**
     * Parses the metadata for all guides in the specified directory.
     *
     * @param guidesDir          the directory containing the guides
     * @param metadataConfigName the name of the metadata configuration file
     * @return a list of parsed guide metadata
     */
    @NonNull
    List<? extends Guide> parseGuidesMetadata(@NonNull @NotNull File guidesDir,
                                              @NonNull @NotNull String metadataConfigName);

    /**
     * Parses the metadata for a single guide in the specified directory.
     *
     * @param guidesDir the directory containing the guides
     * @return an optional containing the parsed guide metadata, or empty if not found
     */
    @NonNull
    default List<? extends Guide> parseGuidesMetadata(@NonNull @NotNull File guidesDir) {
        return parseGuidesMetadata(guidesDir, DEFAULT_METADATA_NAME);
    }

    /**
     * Parses the metadata for a single guide in the specified directory.
     *
     * @param guidesDir          the directory containing the guides
     * @param metadataConfigName the name of the metadata configuration file
     * @return an optional containing the parsed guide metadata, or empty if not found
     */
    @NonNull
    Optional<? extends Guide> parseGuideMetadata(@NonNull @NotNull File guidesDir,
                                                 @NonNull @NotNull String metadataConfigName);
}

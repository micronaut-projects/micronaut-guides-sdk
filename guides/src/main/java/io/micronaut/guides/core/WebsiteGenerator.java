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
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * Interface for generating the guides website assets from the specified input directory to the specified output directory.
 */
public interface WebsiteGenerator {

    /**
     * Generates the guides website assets from the contents of the input directory and writes it to the output directory.
     *
     * @param inputDirectory  the directory containing the source files for the website
     * @param outputDirectory the directory where the generated website will be written
     * @throws IOException if an I/O error occurs during generation
     */
    void generate(
            @NonNull @NotNull File inputDirectory,
            @NonNull @NotNull File outputDirectory) throws IOException;


    /**
     * Generates the guides website assets from the contents of the input directory and writes it to the output directory.
     *
     * @param inputDirectory  the directory containing the source files for the website
     * @param outputDirectory the directory where the generated website will be written
     * @param condition the condition to filter the guides against
     * @throws IOException if an I/O error occurs during generation
     */
    void generate(
        @NonNull @NotNull File inputDirectory,
        @NonNull @NotNull File outputDirectory,
        @Nullable Predicate<Guide> condition) throws IOException;
}

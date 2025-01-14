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
import java.io.IOException;

/**
 * GuideProjectGenerator is an interface for generating guide projects.
 */
public interface GuideProjectGenerator {

    /**
     * Generates a guide project in the specified output directory.
     *
     * @param outputDirectory the directory where the project will be generated
     * @param guide           the guide containing the project details
     * @throws IOException if an I/O error occurs during project generation
     */
    void generate(@NotNull @NonNull File outputDirectory, @NotNull @NonNull Guide guide) throws IOException;
}

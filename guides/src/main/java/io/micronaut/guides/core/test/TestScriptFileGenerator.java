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
package io.micronaut.guides.core.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.FileGenerator;
import io.micronaut.guides.core.Guide;
import jakarta.validation.constraints.NotNull;

import java.io.File;

/**
 * saves a bash script to test the guide.
 */
public interface TestScriptFileGenerator extends FileGenerator {
    /**
     * Save a script for running native tests for the given guide.
     *
     * @param outputDirectory the output directory
     * @param guide           the guide metadata
     */
    void saveNativeTestScript(@NotNull @NonNull File outputDirectory, @NonNull @NotNull Guide guide);

    /**
     * Save a script for running tests for the given guides.
     *
     * @param outputDirectory the output directory
     * @param guide           the guide metadata
     */
    void saveTestScript(@NotNull @NonNull File outputDirectory, @NonNull @NotNull Guide guide);
}

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
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Interface for generating test scripts for guides.
 * This interface provides methods to check if an app or language supports native tests,
 * if an app uses the Micronaut framework, and to generate scripts for running tests.
 */
public interface TestScriptGenerator {

    /**
     * Checks if the given app supports native tests.
     *
     * @param app          the app to check
     * @param guidesOption the guides option containing additional configuration
     * @return true if the app supports native tests, false otherwise
     */
    boolean supportsNativeTest(@NonNull @NotNull App app, @NonNull @NotNull GuidesOption guidesOption);

    /**
     * Checks if the given app uses the Micronaut framework.
     *
     * @param app the app to check
     * @return true if the app uses the Micronaut framework, false otherwise
     */
    boolean isMicronautFramework(@NonNull @NotNull App app);

    /**
     * Checks if the given language supports native tests.
     *
     * @param language the language to check
     * @return true if the language supports native tests, false otherwise
     */
    boolean supportsNativeTest(@NonNull @NotNull Language language);

    /**
     * Generates a script for running native tests for the given guides.
     *
     * @param metadatas the list of guides metadata
     * @return the generated script as a string
     */
    @NonNull
    @NotNull
    String generateNativeTestScript(@NonNull @NotNull List<? extends Guide> metadatas);

    /**
     * Generates a script for running tests for the given guides.
     *
     * @param metadatas the list of guides metadata
     * @return the generated script as a string
     */
    @NonNull
    @NotNull
    String generateTestScript(@NonNull @NotNull List<? extends Guide> metadatas);

}

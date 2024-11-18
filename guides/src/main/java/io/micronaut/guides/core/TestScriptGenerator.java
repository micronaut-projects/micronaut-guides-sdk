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

public interface TestScriptGenerator {

    boolean supportsNativeTest(@NonNull @NotNull App app, @NonNull @NotNull GuidesOption guidesOption);

    boolean isMicronautFramework(@NonNull @NotNull App app);

    boolean supportsNativeTest(@NonNull @NotNull Language language);

    @NonNull
    @NotNull
    String generateNativeTestScript(@NonNull @NotNull List<Guide> metadatas);

    @NonNull
    @NotNull
    String generateTestScript(@NonNull @NotNull List<Guide> metadatas);

}

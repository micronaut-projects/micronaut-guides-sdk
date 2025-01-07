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
package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.File;

/**
 * AsciidocConverter is an interface that defines methods for converting Asciidoc files.
 */
public interface AsciidocConverter {
    /**
     * Converts the source Asciidoc file to the specified destination file.
     *
     * @param asciidoc the source Asciidoc, must not be null
     * @param baseDir Base directory for asciidoc
     * @param attributeProvider Attribute provider
     * @return the converted content as a string, or null if the conversion fails
     */
    @Nullable
    String convert(@NonNull @NotBlank String asciidoc,
                   @NonNull @NotNull File baseDir,
                   @NonNull AsciidocAttributeProvider attributeProvider);
}

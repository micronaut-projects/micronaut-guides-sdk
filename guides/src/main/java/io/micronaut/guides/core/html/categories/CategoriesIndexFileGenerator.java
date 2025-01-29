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
package io.micronaut.guides.core.html.categories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.FileGenerator;
import io.micronaut.guides.core.Guide;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;

/**
 * Save a category index HTML file into an output directory.
 */
public interface CategoriesIndexFileGenerator extends FileGenerator {
    /**
     * Save a category index HTML file into an output directory.
     * @param guides Guides
     * @param outputDirectory Output directory
     */
    void saveCategoryIndex(@NonNull @NotNull @NotEmpty List<? extends Guide> guides,
                     @NonNull @NotNull File outputDirectory);
}

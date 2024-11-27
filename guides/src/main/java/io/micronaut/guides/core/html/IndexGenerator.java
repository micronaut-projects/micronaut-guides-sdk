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
package io.micronaut.guides.core.html;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.Guide;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Interface for generating an index page for a list of guides.
 */
public interface IndexGenerator {

    /**
     * Renders the index page for the given list of guides.
     *
     * @param guides the list of guides to render the index for
     * @return the rendered index as a string
     */
    @NonNull
    String renderIndex(@NonNull @NotNull List<? extends Guide> guides);
}

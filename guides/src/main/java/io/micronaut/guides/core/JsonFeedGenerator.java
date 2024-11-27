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

import java.io.IOException;
import java.util.List;

/**
 * JsonFeedGenerator is an interface that defines a method for generating a JSON feed string
 * from a list of Guide metadata objects.
 */
public interface JsonFeedGenerator {
    /**
     * Generates a JSON feed string from the provided list of Guide metadata objects.
     *
     * @param metadatas the list of Guide metadata objects
     * @return the generated JSON feed string
     * @throws IOException if an I/O error occurs during the generation of the JSON feed
     */
    @NonNull
    String jsonFeedString(@NonNull List<? extends Guide> metadatas) throws IOException;
}

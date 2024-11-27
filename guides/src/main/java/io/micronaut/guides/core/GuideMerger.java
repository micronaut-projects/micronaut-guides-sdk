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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface for merging guide metadata.
 */
public interface GuideMerger {
    /**
     * Merges a list of guide metadata.
     *
     * @param metadatas The list of guide metadata to be merged.
     * @param <T>       The type of the guide metadata objects.
     */
    <T extends Guide> void mergeGuides(List<T> metadatas);

    /**
     * Merges multiple collections into one list.
     *
     * @param lists An array of Collection objects to be merged.
     * @param <T>   The type of the guide metadata objects.
     * @return A single List containing all elements from the provided Collections, excluding any null values.
     */
    static <T> List<T> mergeLists(List<? extends T>... lists) {
        Set<T> mergedSet = new LinkedHashSet<>();
        for (List<? extends T> list : lists) {
            if (list != null) {
                mergedSet.addAll(list);
            }
        }
        return new ArrayList<>(mergedSet);
    }
}

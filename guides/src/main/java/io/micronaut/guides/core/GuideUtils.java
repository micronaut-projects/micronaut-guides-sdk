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

import java.util.Collection;
import java.util.List;

/**
 * GuideUtils is a utility class that provides various helper methods for handling guides.
 * It contains methods for retrieving tags, features, frameworks, and merging guide data.
 * This class cannot be instantiated.
 */
public final class GuideUtils {
    public static final String MICRONAUT_PREFIX = "micronaut-";
    public static final String VIEWS_PREFIX = "views-";
    public static final List<String> FEATURES_PREFIXES = List.of(MICRONAUT_PREFIX, VIEWS_PREFIX);

    private GuideUtils() { }

    /**
     * Adds all elements from the source collection to the target collection safely.
     *
     * @param target The collection where elements will be added.
     * @param src    The collection whose elements are to be added to the target.
     * @throws NullPointerException If the target collection is null.
     */
    static void addAllSafe(Collection<String> target, Collection<String> src) {
        if (target == null) {
            throw new NullPointerException("Target list cannot be null");
        }

        if (src != null) {
            target.addAll(src);
        }
    }
}

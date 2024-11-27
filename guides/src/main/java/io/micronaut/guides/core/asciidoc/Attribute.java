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

import java.util.ArrayList;
import java.util.List;

/**
 * Attribute is a record that represents an Asciidoc attribute with a key and a list of values.
 *
 * @param key    the key of the attribute
 * @param values the list of values associated with the key
 */
public record Attribute(String key, List<String> values) {
    private static final String ATTRIBUTE_SEPARATOR = ",";
    private static final String VALUE_SEPARATOR = ";";
    private static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * Parses a string to create a list of Attribute instances.
     *
     * @param str the string to parse, must not be null
     * @return a list of Attribute instances
     */
    @NonNull
    public static List<Attribute> of(@NonNull String str) {
        List<Attribute> result = new ArrayList<>();
        String[] arr = str.split(ATTRIBUTE_SEPARATOR);
        for (String attributeString : arr) {
            int index = attributeString.indexOf(KEY_VALUE_SEPARATOR);
            if (index != -1) {
                String key = attributeString.substring(0 , index);
                String value = attributeString.substring(index + KEY_VALUE_SEPARATOR.length());
                String[] values = value.split(VALUE_SEPARATOR);
                result.add(new Attribute(key, List.of(values)));
            }
        }
        return result;
    }
}

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

import java.util.List;
import java.util.Optional;

/**
 * AsciidocMacro is a record that represents an Asciidoc macro.
 *
 * @param name       the name of the macro
 * @param target     the target of the macro
 * @param attributes the list of attributes associated with the macro
 */
public record AsciidocMacro(String name, String target, List<Attribute> attributes, String raw) {
    public static final String MACRO_NAME_SEPARATOR = ":";
    public static final String ATTRIBUTE_SEPARATOR = ",";
    private static final String MACRO_OPEN_BRACKET = "[";
    private static final String MACRO_CLOSE_BRACKET = "]";

    /**
     * Creates an AsciidocMacro from the given name and string representation.
     *
     * @param name the name of the macro
     * @param str  the string representation of the macro
     * @return an Optional containing the AsciidocMacro if the string representation is valid, otherwise an empty Optional
     */
    public static Optional<AsciidocMacro> of(String name, String str) {
        int bracketIndex = str.indexOf(MACRO_OPEN_BRACKET);
        int closingBracketIndex = str.indexOf(MACRO_CLOSE_BRACKET);
        if (str.startsWith(name + MACRO_NAME_SEPARATOR) && bracketIndex != -1 && closingBracketIndex != -1) {
            String target = str.substring((name + MACRO_NAME_SEPARATOR).length(), bracketIndex);
            String attributeLine = str.substring(bracketIndex + 1, closingBracketIndex);
            List<Attribute> attributes = Attribute.of(attributeLine);
            return Optional.of(new AsciidocMacro(name, target, attributes, str));
        }
        return Optional.empty();
    }
}

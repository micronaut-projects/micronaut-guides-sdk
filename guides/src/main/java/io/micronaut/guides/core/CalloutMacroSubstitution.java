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

import jakarta.inject.Singleton;

/**
 * CalloutMacroSubstitution is a singleton class that extends LineMacroSubstitution.
 * It provides methods to translate the custom "callout" syntax into Asciidoc.
 */
@Singleton
public class CalloutMacroSubstitution extends LineMacroSubstitution {

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    protected String getMacroName() {
        return "callout";
    }

    /**
     * Returns the base directory for the macro substitution.
     *
     * @return the base directory
     */
    @Override
    protected String getBaseDirectory() {
        return "{calloutsDir}";
    }

    /**
     * Returns the prefix for the macro substitution.
     *
     * @return the prefix
     */
    @Override
    protected String getPrefix() {
        return "callout-";
    }
}

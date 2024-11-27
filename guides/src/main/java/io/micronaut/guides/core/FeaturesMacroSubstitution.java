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

import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import jakarta.inject.Singleton;

/**
 * Class that provides macro substitution functionality for feature placeholders in guide templates.
 */
@Singleton
public class FeaturesMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    protected String getMacroName() {
        return "features";
    }

    /**
     * Returns the substitution string for the given guide, option, and application name.
     *
     * @param guide   the guide metadata
     * @param option  the guide option
     * @param appName the application name
     * @return the substitution string
     */
    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        return String.join(AsciidocMacro.ATTRIBUTE_SEPARATOR, app(guide, appName).visibleFeatures(option.getLanguage()));
    }
}

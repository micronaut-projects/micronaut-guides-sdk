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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that provides macro substitution functionality for features words in guide templates.
 */
@Singleton
public class FeaturesWordsMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    protected String getMacroName() {
        return "features-words";
    }

    /**
     * Substitutes placeholders with features words in the given guide and option.
     *
     * @param guideRender guide
     * @param appName the application name
     * @return the string with features words substituted
     */
    @Override
    protected String getSubstitution(GuideRender guideRender, String appName) {
        List<String> features = app(guideRender.guide(), appName).visibleFeatures(guideRender.option().getLanguage())
                .stream()
                .map(feature -> "`" + feature + "`")
                .collect(Collectors.toList());

        if (features.size() > 1) {
            return String.join(", ", features.subList(0, features.size() - 1)) + ", and " + features.get(features.size() - 1);
        }
        return features.get(0);
    }
}

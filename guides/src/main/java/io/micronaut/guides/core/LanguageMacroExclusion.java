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

/**
 * LanguageMacroExclusion is a singleton class that extends MacroExclusion.
 * It provides methods to handle macro exclusions based on the programming language.
 */
@Singleton
public class LanguageMacroExclusion extends MacroExclusion {
    /**
     * Macro name for language exclusion.
     */
    private static final String MACRO_LANGUAGE_EXCLUSION = "exclude-for-languages";

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    protected String getMacroName() {
        return MACRO_LANGUAGE_EXCLUSION;
    }

    /**
     * Determines whether the macro should be excluded based on the specified parameters, option, and guide.
     *
     * @param params the list of parameters
     * @param guideRender guide
     * @return true if the macro should be excluded, false otherwise
     */
    @Override
    protected boolean shouldExclude(List<String> params, GuideRender guideRender) {
        return params.contains(guideRender.option().getLanguage().toString());
    }
}

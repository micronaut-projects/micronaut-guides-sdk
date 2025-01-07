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
 * C that provides macro exclusion functionality for build tools.
 */
@Singleton
public class BuildMacroExclusion extends MacroExclusion {
    private static final String MACRO_BUILD_EXCLUSION = "exclude-for-build";

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    protected String getMacroName() {
        return MACRO_BUILD_EXCLUSION;
    }

    /**
     * Determines whether the given parameters should be excluded based on the build tool option.
     *
     * @param params the list of parameters
     * @param guideRender Guide
     * @return true if the parameters should be excluded, false otherwise
     */
    @Override
    protected boolean shouldExclude(List<String> params, GuideRender guideRender) {
        return params.contains(guideRender.option().getBuildTool().toString());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

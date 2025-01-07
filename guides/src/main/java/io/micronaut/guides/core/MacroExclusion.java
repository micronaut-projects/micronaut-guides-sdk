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

import java.util.List;

abstract class MacroExclusion implements MacroSubstitution {
    private static final String LINE_BREAK = "\n";

    /**
     * Gets the name of the macro.
     *
     * @return the name of the macro
     */
    protected abstract String getMacroName();

    /**
     * Determines whether the exclusion applies for this guide and option.
     *
     * @param params the list of parameters for the macro
     * @param guideRender  the Guide
     * @return true if the exclusion applies for this guide and option, false otherwise
     */
    protected abstract boolean shouldExclude(List<String> params, GuideRender guideRender);

    @Override
    public String substitute(String str, GuideRender guideRender) {
        for (List<String> group : MacroUtils.findMacroGroupsNested(str, getMacroName())) {
            if (!group.isEmpty()) {
                List<String> params = MacroUtils.extractMacroGroupParameters(group.get(0), getMacroName());
                if (shouldExclude(params, guideRender)) {
                    str = str.replace(String.join(LINE_BREAK, group) + LINE_BREAK, "");
                } else {
                    str = str.replace(String.join(LINE_BREAK, group), String.join(LINE_BREAK, group.subList(1, group.size() - 1)));
                }
            }

        }
        return str;
    }
}

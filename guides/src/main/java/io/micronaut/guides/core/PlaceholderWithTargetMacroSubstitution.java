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

import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.asciidoc.PlacheholderMacro;

import java.util.Optional;
import java.util.regex.Pattern;

abstract class PlaceholderWithTargetMacroSubstitution implements MacroSubstitution {

    /**
     * Gets the name of the macro.
     *
     * @return the name of the macro
     */
    protected abstract String getMacroName();

    /**
     * Gets the substitution string for the given guide, option, and app.
     *
     * @param guideRender Guide
     * @param app    the application name or target
     * @return the substitution string
     */
    protected abstract String getSubstitution(GuideRender guideRender, String app);

    public String substitute(String str, GuideRender guideRender) {
        Pattern pattern = Pattern.compile("@(?:([\\w-]*):)?" + getMacroName() + "@");
        for (String instance : MacroUtils.findMacroInstances(str, pattern)) {
            Optional<PlacheholderMacro> macroOptional = PlacheholderMacro.of(getMacroName(), instance);
            if (macroOptional.isEmpty()) {
                continue;
            }
            PlacheholderMacro macro = macroOptional.get();
            String app = StringUtils.isNotEmpty(macro.target()) ? macro.target() : "default";
            String res = getSubstitution(guideRender, app);
            str = str.replace(instance, res);
        }
        return str;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

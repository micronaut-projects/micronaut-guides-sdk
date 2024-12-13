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

import static io.micronaut.guides.core.MacroUtils.findMacroGroups;
import static io.micronaut.guides.core.MacroUtils.findMacroLines;

/**
 * Class that provides macro substitution functionality for dependency placeholders in guide templates.
 */
@Singleton
public class DependencyMacroSubstitution implements MacroSubstitution {

    /**
     * Substitutes dependency placeholders in the given string with Asciidoc formatted dependency lines.
     *
     * @param str    the string containing dependency placeholders
     * @param guideRender guide
     * @return the string with dependency placeholders substituted
     */
    @Override
    public String substitute(String str, GuideRender guideRender) {
        for (String block : findMacroGroups(str, "dependencies")) {
            List<String> lines = DependencyLines.asciidoc(block.replace(":dependencies:", "").strip().lines().toList(), guideRender.option().getBuildTool(), guideRender.option().getLanguage());
            str = str.replace(block, String.join("\n", lines));
        }
        for (String line : findMacroLines(str, "dependency")) {
            List<String> lines = DependencyLines.asciidoc(line, guideRender.option().getBuildTool(), guideRender.option().getLanguage());
            str = str.replace(line, String.join("\n", lines));
        }
        return str;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

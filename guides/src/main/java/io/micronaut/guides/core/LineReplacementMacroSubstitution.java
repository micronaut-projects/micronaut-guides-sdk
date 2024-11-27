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
import java.util.Optional;
import static io.micronaut.guides.core.MacroUtils.findMacroLines;

/**
 * Line Replacement Macro Substitution.
 */
public abstract class LineReplacementMacroSubstitution implements MacroSubstitution {
    /**
     *
     * @return Macro name
     */
    protected abstract String getMacro();

    /**
     *
     * @param asciidocMacro Asciidoc Macro
     * @param guide Guide
     * @param option Guide Option
     * @return Line replacement
     */
    protected abstract String replacement(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option);

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for (String line : findMacroLines(str, getMacro())) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(getMacro(), line);
            if (asciidocMacroOptional.isEmpty()) {
                continue;
            }

            AsciidocMacro asciidocMacro = asciidocMacroOptional.get();
            str = str.replace(line, replacement(asciidocMacro, guide, option));
        }
        return str;
    }

}

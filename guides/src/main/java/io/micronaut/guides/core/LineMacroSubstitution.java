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

import io.micronaut.guides.core.asciidoc.Argument;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import io.micronaut.guides.core.asciidoc.IncludeDirective;

import java.nio.file.Path;
import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

abstract class LineMacroSubstitution implements MacroSubstitution {
    /**
     * Gets the name of the macro.
     *
     * @return the name of the macro
     */
    protected abstract String getMacroName();

    /**
     * Gets the base directory for the macro substitution.
     *
     * @return the base directory
     */
    protected abstract String getBaseDirectory();

    /**
     * Gets the prefix to be used in the macro substitution.
     *
     * @return the prefix
     */
    protected abstract String getPrefix();

    @Override
    public String substitute(String str, GuideRender guideRender) {
        for (String line : findMacroLines(str, getMacroName())) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(getMacroName(), line);
            if (asciidocMacroOptional.isEmpty()) {
                continue;
            }

            AsciidocMacro macro = asciidocMacroOptional.get();
            StringBuilder builder = new StringBuilder();

            for (Attribute attribute : macro.attributes()) {
                Argument argument = new Argument(attribute.key(), attribute.values().getFirst());
                builder.append(argument).append("\n");
            }

            Path target = Path.of(getBaseDirectory(), getPrefix() + macro.target());

            IncludeDirective.Builder includeDirectiveBuilder = IncludeDirective.builder().target(target.toString());
            builder.append(includeDirectiveBuilder.build());

            str = str.replace(line, builder.toString());
        }
        return str;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

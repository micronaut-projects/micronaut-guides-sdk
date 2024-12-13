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

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * A composite implementation of the {@link MacroSubstitution} interface that applies a list of
 * {@link MacroSubstitution} instances in sequence.
 */
@Singleton
@Primary
@Internal
class CompositeMacroSubstitution implements MacroSubstitution {
    private final List<MacroSubstitution> substitutions;

    /**
     * Constructs a new {@code CompositeMacroSubstitution} with the given list of substitutions.
     *
     * @param substitutions the list of {@link MacroSubstitution} instances to apply
     */
    CompositeMacroSubstitution(List<MacroSubstitution> substitutions) {
        this.substitutions = substitutions;
    }

    /**
     * Applies the list of {@link MacroSubstitution} instances to the given string in sequence.
     *
     * @param str    the string to substitute
     * @param guideRender Guide
     * @return the substituted string
     */
    @Override
    @NonNull
    public String substitute(@NonNull String str, @NonNull GuideRender guideRender) {
        String result = str;
        for (MacroSubstitution substitution : substitutions) {
            result = substitution.substitute(result, guideRender);
        }
        return result;
    }
}

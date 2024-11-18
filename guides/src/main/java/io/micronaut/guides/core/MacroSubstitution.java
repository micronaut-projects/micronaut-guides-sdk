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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;

public interface MacroSubstitution {
    String ATTRIBUTE_APP = "app";
    String APP_NAME_DEFAULT = "default";

    @NonNull
    String substitute(@NonNull String str, @NonNull Guide guide, @NonNull GuidesOption option);

    default App app(Guide guide, AsciidocMacro asciidocMacro) {
        return app(guide, appName(asciidocMacro));
    }

    default App app(Guide guide, String appName) {
        return guide.apps().stream()
                .filter(a -> a.name().equals(appName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("app not found for app name" + appName));
    }

    default String appName(AsciidocMacro asciidocMacro) {
        return asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(ATTRIBUTE_APP))
                .map(Attribute::values)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0))
                .findFirst()
                .orElse(APP_NAME_DEFAULT);
    }
}

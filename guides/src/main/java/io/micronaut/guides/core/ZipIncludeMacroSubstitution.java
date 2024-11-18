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

import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;

@Singleton
public class ZipIncludeMacroSubstitution extends SourceBlockMacroSubstitution {
    private static final String MACRO_ZIPINCLUDE = "zipInclude";

    public ZipIncludeMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() {
        return MACRO_ZIPINCLUDE;
    }

    @Override
    protected Classpath getClasspath() {
        return Classpath.MAIN;
    }

    @Override
    protected FileType getFileType() {
        return FileType.RESOURCE;
    }

    @Override
    protected String sourceTitle(
            String appName,
            String condensedTarget,
            Classpath classpath,
            String language,
            String packageName) {
        return condensedTarget;
    }
}

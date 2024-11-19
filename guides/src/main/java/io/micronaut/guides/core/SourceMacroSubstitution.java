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

/**
 * SourceMacroSubstitution is a singleton class that extends SourceBlockMacroSubstitution.
 * It provides the implementation for the "source" macro used in the guides.
 */
@Singleton
public class SourceMacroSubstitution extends SourceBlockMacroSubstitution {
    private static final String MACRO_SOURCE = "source";

    /**
     * Constructs a new SourceMacroSubstitution with the specified GuidesConfiguration and LicenseLoader.
     *
     * @param guidesConfiguration the guides configuration
     * @param licenseLoader       the license loader
     */
    public SourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    public String getMacroName() {
        return MACRO_SOURCE;
    }

    /**
     * Returns the classpath for the macro.
     *
     * @return the classpath
     */
    @Override
    public Classpath getClasspath() {
        return Classpath.MAIN;
    }

    /**
     * Returns the file type for the macro.
     *
     * @return the file type
     */
    @Override
    public FileType getFileType() {
        return FileType.CODE;
    }
}

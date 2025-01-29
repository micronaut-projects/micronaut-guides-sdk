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
 * ZipIncludeMacroSubstitution is a singleton class that extends SourceBlockMacroSubstitution.
 * It provides methods to substitute the "zipInclude" macro in Asciidoc files.
 */
@Singleton
public class ZipIncludeMacroSubstitution extends SourceBlockMacroSubstitution {
    private static final String MACRO_ZIPINCLUDE = "zipInclude";

    /**
     * Constructs a new ZipIncludeMacroSubstitution with the specified guides configuration and license loader.
     *
     * @param guidesConfiguration the guides configuration
     * @param licenseLoader       the license loader
     * @param guideSourceService Guide Source Service
     */
    public ZipIncludeMacroSubstitution(GuidesConfiguration guidesConfiguration,
                                       LicenseLoader licenseLoader,
                                       GuideSourceService guideSourceService) {
        super(licenseLoader, guidesConfiguration, guideSourceService);
    }

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    public String getMacroName() {
        return MACRO_ZIPINCLUDE;
    }

    /**
     * Returns the classpath for the macro substitution.
     *
     * @return the classpath
     */
    @Override
    protected Classpath getClasspath() {
        return Classpath.MAIN;
    }

    /**
     * Returns the file type for the macro substitution.
     *
     * @return the file type
     */
    @Override
    protected FileType getFileType() {
        return FileType.RESOURCE;
    }

    /**
     * Returns the source title for the macro substitution.
     *
     * @param appName         the application name
     * @param condensedTarget the condensed target
     * @param classpath       the classpath
     * @param language        the language
     * @param packageName     the package name
     * @return the source title
     */
    @Override
    protected String sourceTitle(String appName, String condensedTarget, Classpath classpath, String language, String packageName) {
        return condensedTarget;
    }
}

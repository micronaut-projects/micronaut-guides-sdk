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

/**
 * RawTestMacroSubstitution is a singleton class that extends TestMacroSubstitution.
 * It provides methods to substitute the "rawTest" macro in Asciidoc files.
 */
@Singleton
public class RawTestMacroSubstitution extends TestMacroSubstitution {
    private static final String MACRO_RAW_TEST = "rawTest";

    /**
     * Constructs a new RawTestMacroSubstitution with the specified GuidesConfiguration and LicenseLoader.
     *
     * @param guidesConfiguration the guides configuration
     * @param licenseLoader       the license loader
     * @param guideSourceService Guide Source Service
     */
    public RawTestMacroSubstitution(GuidesConfiguration guidesConfiguration,
                                    LicenseLoader licenseLoader,
                                    GuideSourceService guideSourceService) {
        super(guidesConfiguration, licenseLoader, guideSourceService);
    }

    /**
     * Returns the name of the macro.
     *
     * @return the macro name
     */
    @Override
    public String getMacroName() {
        return MACRO_RAW_TEST;
    }

    /**
     * Returns the language for the macro substitution based on the provided GuidesOption.
     *
     * @param option the guides option
     * @return the language as a string
     */
    @Override
    protected String getLanguage(GuidesOption option) {
        return option.getTestFramework().toTestFramework().getDefaultLanguage().toString();
    }

    /**
     * Returns the file extension for the macro substitution based on the provided GuidesOption.
     *
     * @param option the guides option
     * @return the file extension as a string
     */
    @Override
    protected String getExtension(GuidesOption option) {
        return option.getTestFramework().toTestFramework().getDefaultLanguage().getExtension();
    }
}

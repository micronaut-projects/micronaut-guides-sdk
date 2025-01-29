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
import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;

import static io.micronaut.starter.api.TestFramework.SPOCK;

/**
 * TestMacroSubstitution is a singleton class that extends SourceBlockMacroSubstitution.
 * It provides methods to handle macro substitutions for test code blocks in Asciidoc files.
 */
@Singleton
public class TestMacroSubstitution extends SourceBlockMacroSubstitution {
    /**
     * Suffix used for Spock specification files.
     */
    public static final String SUFFIX_SPEC = "Spec";

    /**
     * Macro name for test blocks.
     */
    private static final String MACRO_TEST = "test";

    /**
     * Suffix used for test files.
     */
    private static final String SUFFIX_TEST = "Test";

    /**
     * Constructs a new TestMacroSubstitution with the specified guides configuration and license loader.
     *
     * @param guidesConfiguration the guides configuration
     * @param licenseLoader       the license loader
     * @param guideSourceService Guide Source Service
     */
    public TestMacroSubstitution(GuidesConfiguration guidesConfiguration,
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
        return MACRO_TEST;
    }

    /**
     * Returns the classpath for the macro.
     *
     * @return the classpath
     */
    @Override
    public Classpath getClasspath() {
        return Classpath.TEST;
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

    /**
     * Condenses the target of the Asciidoc macro based on the specified options.
     *
     * @param asciidocMacro the Asciidoc macro
     * @param option        the guides option
     * @return the condensed target
     */
    @Override
    public String condensedTarget(@NonNull AsciidocMacro asciidocMacro, GuidesOption option) {
        String target = asciidocMacro.target();
        if (target.endsWith(SUFFIX_TEST)) {
            target = target.substring(0, target.indexOf(SUFFIX_TEST));
            target += option.getTestFramework() == SPOCK ? SUFFIX_SPEC : SUFFIX_TEST;
        }
        return target;
    }
}

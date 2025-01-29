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
package io.micronaut.guides.core.test;

import io.micronaut.core.annotation.Internal;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesConfiguration;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;

@Internal
@Singleton
class DefaultTestScriptFileGenerator implements TestScriptFileGenerator {
    private final TestScriptGenerator testScriptGenerator;
    private final GuidesConfiguration guidesConfiguration;

    DefaultTestScriptFileGenerator(TestScriptGenerator testScriptGenerator,
                                   GuidesConfiguration guidesConfiguration) {
        this.testScriptGenerator = testScriptGenerator;
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public void saveNativeTestScript(File outputDirectory, Guide guide) {
        String script = testScriptGenerator.generateNativeTestScript(outputDirectory, guide);
        try {
            saveFile(script, guide.getOutputDirectory(outputDirectory), guidesConfiguration.getNativeTestFileName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveTestScript(File outputDirectory, Guide guide) {
        String script = testScriptGenerator.generateTestScript(outputDirectory, guide);
        try {
            saveFile(script, guide.getOutputDirectory(outputDirectory), guidesConfiguration.getTestFileName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

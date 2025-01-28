/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.guides.core.html;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.FileGenerator;
import io.micronaut.guides.core.Guide;

import java.io.File;
import java.io.IOException;

/**
 * GuidePageGenerator is an interface for generating guide pages.
 */
public interface GuidePageGenerator extends FileGenerator {
    /**
     * Generates a guide page in the specified output directory.
     *
     * @param guide           the guide containing the page details
     * @param inputDirectory  the directory containing the page files
     * @param outputDirectory the directory where the page will be generated
     * @throws IOException if an I/O error occurs during page generation
     */
    void generatePage(@NonNull Guide guide, @NonNull File inputDirectory, @NonNull File outputDirectory) throws IOException;
}

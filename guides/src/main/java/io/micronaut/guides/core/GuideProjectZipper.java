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

import java.io.File;
import java.io.IOException;

/**
 * Interface for zipping a directory into a single output file.
 */
public interface GuideProjectZipper {

    /**
     * Zips the contents of the specified source directory into the specified output file.
     *
     * @param guide           the guide to zip
     * @param guideOutput     the guide output directory
     * @param outputDirectory the output directory
     * @throws IOException if an I/O error occurs during zipping
     */
    void zipGuide(@NonNull Guide guide, @NonNull File guideOutput, @NonNull File outputDirectory) throws IOException;


    /**
     * Returns the name of the zip file for the given guide.
     *
     * @param guide   the guide
     * @param options the options
     * @return the name of the zip file
     */
    String getZipFileName(@NonNull Guide guide, @NonNull GuidesOption options);
}

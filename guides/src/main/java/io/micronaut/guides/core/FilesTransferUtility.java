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
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * FilesTransferUtility is an interface that defines a method to transfer files from an input directory to an output directory.
 */
public interface FilesTransferUtility {

    /**
     * Transfers files from the specified input directory to the specified output directory based on the provided guide.
     *
     * @param inputDirectory  the directory containing the files to be transferred
     * @param outputDirectory the directory where the files will be transferred to
     * @param guide           the guide containing the transfer instructions
     * @throws IOException if an I/O error occurs during the file transfer
     */
    void transferFiles(@NotNull @NonNull File inputDirectory,
                       @NotNull @NonNull File outputDirectory,
                       @NotNull @NonNull Guide guide,
                       @NotNull @NonNull List<? extends Guide> guides) throws IOException;
}

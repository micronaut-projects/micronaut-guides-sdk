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

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for loading templates.
 */
public final class TemplateLoaderUtils {
    private TemplateLoaderUtils() {
    }

    /**
     *
     * @param resourceLoader Resource Loader
     * @param guidesTemplatesConfiguration Guides Template Configuration
     * @param templateName Template Name
     * @return Classpath Template
     */
    public static String getClasspathTemplate(ResourceLoader resourceLoader,
                                     GuidesTemplatesConfiguration guidesTemplatesConfiguration,
                                     String templateName) {
        String path = "classpath:" + guidesTemplatesConfiguration.getFolder() + "/" + templateName;
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path);
        if (inputStreamOptional.isEmpty()) {
            throw new ConfigurationException(path);
        }
        try (InputStream inputStream = inputStreamOptional.get()) {
            return readInputStream(inputStream);
        } catch (Exception e) {
            throw new ConfigurationException("Error loading resource: " + path, e);
        }
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}

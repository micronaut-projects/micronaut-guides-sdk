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
package io.micronaut.guides.core.html;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesTemplatesConfiguration;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link IndexGenerator} interface.
 * This class is responsible for generating an index for a list of guides.
 */
@Singleton
public class DefaultIndexGenerator implements IndexGenerator {

    private final String guideHtml;

    public DefaultIndexGenerator(ResourceLoader resourceLoader,
                                 GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        String path = "classpath:" + guidesTemplatesConfiguration.getFolder() + "/guides.html";
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path);
        if (inputStreamOptional.isEmpty()) {
            throw new ConfigurationException(path);
        }
        try (InputStream inputStream = inputStreamOptional.get()) {
            this.guideHtml = readInputStream(inputStream);
        } catch (Exception e) {
            throw new ConfigurationException("Error loading resource: " + path, e);
        }
    }

    protected static String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    @Override
    @NonNull
    public String renderIndex(@NonNull @NotNull List<? extends Guide> guides) {
        return guideHtml.replace("{content}", guidesContent(guides));
    }

    /**
     * @param guides Guides
     * @return HTML content for the guides list
     */
    protected String guidesContent(@NonNull List<? extends Guide> guides) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Guide guide : guides) {
            sb.append(guideContent(guide));
        }
        sb.append("</ul>");
        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * @param guide Guide
     * @return HTML content for an individual guide
     */
    protected String guideContent(@NonNull Guide guide) {
        StringBuilder sb = new StringBuilder();
        String href = guide.getSlug() + ".html";
        String title = guide.getTitle();
        sb.append("<li>");
        sb.append("<a href=\"");
        sb.append(href);
        sb.append("\">");
        sb.append(title);
        sb.append("</a>");
        sb.append("</li>");
        return sb.toString();
    }
}

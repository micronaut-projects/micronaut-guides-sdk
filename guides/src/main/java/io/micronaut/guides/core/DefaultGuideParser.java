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

import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class that provides methods to parse guide metadata.
 */
@Singleton
public class DefaultGuideParser implements GuideParser {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideParser.class);
    protected final GuidesConfiguration guidesConfiguration;
    protected final JsonSchema jsonSchema;
    protected final JsonMapper jsonMapper;
    protected final GuideMerger guideMerger;

    /**
     * Constructs a new DefaultGuideParser.
     *
     * @param jsonSchemaProvider the JSON schema provider
     * @param jsonMapper         the JSON mapper
     * @param guideMerger        the guide merger
     */
    public DefaultGuideParser(GuidesConfiguration guidesConfiguration,
                              JsonSchemaProvider jsonSchemaProvider,
                              JsonMapper jsonMapper,
                              GuideMerger guideMerger) {
        this.guidesConfiguration = guidesConfiguration;
        this.jsonSchema = guidesConfiguration.isValidateMetadata() ? jsonSchemaProvider.getSchema() : null;
        this.jsonMapper = jsonMapper;
        this.guideMerger = guideMerger;
    }

    @Override
    @NonNull
    public List<? extends Guide> parseGuidesMetadata(@NonNull @NotNull File guidesDir, @NonNull @NotNull String metadataConfigName) {
        List<Guide> metadatas = new ArrayList<>();

        File[] dirs = guidesDir.listFiles(File::isDirectory);
        if (dirs == null) {
            return metadatas;
        }
        for (File dir : dirs) {
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add);
        }

        guideMerger.mergeGuides(metadatas);

        return metadatas;
    }

    @Override
    @NonNull
    public Optional<? extends Guide> parseGuideMetadata(@NonNull @NotNull File guidesDir,
                                                        @NonNull @NotNull String metadataConfigName) {
        File configFile = new File(guidesDir, metadataConfigName);
        if (!configFile.exists()) {
            LOG.warn("metadata file not found for {}", guidesDir.getName());
            return Optional.empty();
        }

        String content;
        try {
            content = Files.readString(Paths.get(configFile.toString()));
        } catch (IOException e) {
            LOG.warn("metadata file not found for {}", guidesDir.getName());
            return Optional.empty();
        }

        return readGuide(guidesDir, content, configFile);
    }

    /**
     * @param guidesDir  Guides directory
     * @param content    Metadata content
     * @param configFile Configuration file
     * @return Guide
     */
    protected Optional<? extends Guide> readGuide(File guidesDir, String content, File configFile) {
        Guide guide;
        try {
            guide = jsonMapper.readValue(content, Guide.class);
            if (guide.isPublish() && jsonSchema != null) {
                Set<ValidationMessage> assertions = jsonSchema.validate(content, InputFormat.JSON);

                if (!assertions.isEmpty()) {
                    LOG.trace("Guide metadata {} does not validate the JSON Schema. Skipping guide.", configFile);
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            LOG.trace("Error parsing guide metadata {}. Skipping guide.", configFile, e);
            return Optional.empty();
        }

        guide.setFolder(guidesDir.getName());
        if (guide.getSlug() == null) {
            guide.setSlug(guidesDir.getName());
        }
        if (guide.getAsciidoctor() == null) {
            guide.setAsciidoctor(guide.isPublish() ? guide.getSlug() + ".adoc" : null);
        }

        return Optional.of(guide);
    }
}

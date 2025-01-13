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
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
     * @param guidesConfiguration Guides Configuration
     * @param jsonSchemaProvider  the JSON schema provider
     * @param jsonMapper          the JSON mapper
     * @param guideMerger         the guide merger
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

        List<File> dirs = walk(guidesDir.getAbsolutePath());
        if (dirs.isEmpty()) {
            return metadatas;
        }

        for (File dir : dirs) {
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add);
        }

        guideMerger.mergeGuides(metadatas);

        return metadatas;
    }

    private List<File> walk(String path) {
        List<File> result = new ArrayList<>();
        File root = new File(path);
        File[] list = root.listFiles();
        if (ArrayUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        for (File f : list) {
            if (f.isDirectory()) {
                result.addAll(walk(f.getAbsolutePath()));
                if (new File(f, "metadata.json").exists()) {
                    result.add(f);
                }
            }
        }
        return result;
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
            if (!validateGuide(guide, content, configFile)) {
                return Optional.empty();
            }
        } catch (IOException e) {
            LOG.trace("Error parsing guide metadata {}. Skipping guide.", configFile, e);
            return Optional.empty();
        }
        populateGuideDefaultMetadata(guidesDir, guide);
        return Optional.of(guide);
    }

    /**
     * @param guide      Guide
     * @param content    Metadata content
     * @param configFile Configuration File
     * @param <T>        Guide
     * @return Whether the guide metadata validates against the JSON Schema
     */
    protected <T extends Guide> boolean validateGuide(T guide, String content, File configFile) {
        if (guidesConfiguration.isValidateMetadata() && guide.isPublish() && jsonSchema != null) {
            Set<ValidationMessage> assertions = jsonSchema.validate(content, InputFormat.JSON);

            if (!assertions.isEmpty()) {
                LOG.trace("Guide metadata {} does not validate the JSON Schema. Skipping guide.", configFile);
                return false;
            }
        }
        return true;
    }

    /**
     * @param guidesDir Guide directory
     * @param guide     Guide
     * @param <T>       Guide
     */
    protected <T extends Guide> void populateGuideDefaultMetadata(File guidesDir, T guide) {
        if (CollectionUtils.isEmpty(guide.getLanguages())) {
            guide.setLanguages(guidesConfiguration.getDefaultLanguages());
        }
        guide.setFolder(guidesDir);
        if (guide.getSlug() == null) {
            guide.setSlug(guidesDir.getName());
        }
        if (guide.getAsciidoctor() == null) {
            guide.setAsciidoctor(guide.isPublish() ? guide.getSlug() + ".adoc" : null);
        }
        for (App app : guide.getApps()) {
            if (StringUtils.isEmpty(app.getName())) {
                app.setName(guidesConfiguration.getDefaultAppName());
            }
            if (StringUtils.isEmpty(app.getPackageName())) {
                app.setPackageName(guidesConfiguration.getPackageName());
            }
        }
    }
}

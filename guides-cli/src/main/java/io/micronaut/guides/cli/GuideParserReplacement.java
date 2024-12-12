package io.micronaut.guides.cli;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.core.*;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import com.networknt.schema.InputFormat;
import com.networknt.schema.ValidationMessage;

@Singleton
@Replaces(GuideParser.class)
public class GuideParserReplacement extends DefaultGuideParser {
    private static final Logger LOG = LoggerFactory.getLogger(GuideParserReplacement.class);

    /**
     * Constructs a new DefaultGuideParser.
     *
     * @param jsonSchemaProvider the JSON schema provider
     * @param jsonMapper         the JSON mapper
     * @param guideMerger        the guide merger
     */
    public GuideParserReplacement(GuidesConfiguration guidesConfiguration,
                                  JsonSchemaProvider jsonSchemaProvider,
                                  JsonMapper jsonMapper,
                                  GuideMerger guideMerger) {
        super(guidesConfiguration, jsonSchemaProvider, jsonMapper, guideMerger);
    }

    protected Optional<? extends Guide> readGuide(File guidesDir, String content, File configFile) {
        GdkGuide guide;
        try {
            guide = jsonMapper.readValue(content, GdkGuide.class);
            if (guide.isPublish()) {
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

        guide.setSlug(guidesDir.getName());
        guide.setAsciidoctor(guide.isPublish() ? guidesDir.getName() + ".adoc" : null);

        return Optional.of(guide);
    }
}

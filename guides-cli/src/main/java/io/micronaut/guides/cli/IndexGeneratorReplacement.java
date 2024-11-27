package io.micronaut.guides.cli;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesTemplatesConfiguration;
import io.micronaut.guides.core.html.DefaultIndexGenerator;
import io.micronaut.guides.core.html.IndexGenerator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Singleton
@Replaces(IndexGenerator.class)
public class IndexGeneratorReplacement extends DefaultIndexGenerator {
    private final String indexItem;

    public IndexGeneratorReplacement(ResourceLoader resourceLoader, GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        super(resourceLoader, guidesTemplatesConfiguration);

        String path = "classpath:" + guidesTemplatesConfiguration.getFolder() + "/index-item.html";
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path);
        if (inputStreamOptional.isEmpty()) {
            throw new ConfigurationException(path);
        }
        try (InputStream inputStream = inputStreamOptional.get()) {
            this.indexItem = readInputStream(inputStream);
        } catch (Exception e) {
            throw new ConfigurationException("Error loading resource: " + path, e);
        }

    }

    @Override
    protected String guidesContent(List<? extends Guide> guides) {
        StringBuilder sb = new StringBuilder();
        for (Guide guide : guides) {
            sb.append(guideContent(guide));
        }
        return sb.toString();
    }

    @Override
    protected String guideContent(Guide guide) {

        String cloud = guide.getCloud() != null ? guide.getCloud().toString().toLowerCase() : "independent";
        String categoryClass = "";
        String categoryTitle = "";
        for (String category : guide.getCategories()) {
            categoryClass = category.toLowerCase().replace(" ", "-");
            categoryTitle = category.toLowerCase();
        }

        StringBuilder sb = new StringBuilder();
        for (Language lang : guide.getLanguages()) {
            for (BuildTool buildTool : guide.getBuildTools()) {
                String build = buildTool.toString().toLowerCase();
                String href = guide.getSlug() + "-" + build + "-" + lang + ".html";
                sb.append(indexItem.replace("{cloud}", cloud)
                        .replace("{categoryClass}", categoryClass)
                        .replace("{categoryTitle}", categoryTitle)
                        .replace("{build}", build)
                        .replace("{title}", guide.getTitle())
                        .replace("{intro}", guide.getIntro())
                        .replace("{href}", href));
            }
        }
        return sb.toString();
    }
}

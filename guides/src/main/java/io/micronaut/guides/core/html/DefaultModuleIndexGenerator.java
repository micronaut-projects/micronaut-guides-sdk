package io.micronaut.guides.core.html;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesTemplatesConfiguration;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class DefaultModuleIndexGenerator implements ModuleIndexGenerator {

    private final String guideHtml;

    public DefaultModuleIndexGenerator(ResourceLoader resourceLoader,
                                       GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        String path = "classpath:" + guidesTemplatesConfiguration.getFolder() + "/modules.html";
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

    @Override
    public String renderIndex(List<? extends Guide> guides) {
        return guideHtml.replace("{content}", guidesContent(guides));
    }

    protected static String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /**
     * @param guides Guides
     * @return HTML content for the guides list
     */
    protected String guidesContent(@NonNull List<? extends Guide> guides) {
        StringBuilder sb = new StringBuilder();
        List<String> modules = guides.stream()
                .flatMap(g -> g.getCategories().stream())
                .distinct()
                .collect(Collectors.toList());
        for (String module : modules) {
            sb.append("<h2>");
            sb.append(module);
            sb.append("</h2>");
            sb.append("<ul>");
            for (Guide guide : guides) {
                if (guide.getCategories().contains(module)) {
                    sb.append(guideContent(guide));
                }
            }
            sb.append("</ul>");
        }
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

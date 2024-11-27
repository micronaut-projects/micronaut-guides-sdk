package io.micronaut.guides.cli;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesConfiguration;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.guides.core.GuidesTemplatesConfiguration;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.html.HtmlUtils;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;

@Singleton
class DownloadMacro extends GradleMavenTabs {

    private final GuidesConfiguration guidesConfiguration;

    public static final String MACRO_DOWNLOAD = "download";
    public static final String BASE_DOWNLOAD_URL = "https://github.com/oracle/graal-dev-kit/releases/download/";

    DownloadMacro(ResourceLoader resourceLoader,
                  GuidesTemplatesConfiguration guidesTemplatesConfiguration,
                  GuidesConfiguration guidesConfiguration) {
        super(resourceLoader, guidesTemplatesConfiguration);
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    protected String getMacro() {
        return MACRO_DOWNLOAD;
    }

    @Override
    protected String gradle(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        String downloadTitle = capitalizeWords(guide.getSlug().replace("-", " "));
        String relativePathToGradleDownload = UriBuilder.of(BASE_DOWNLOAD_URL)
                .path(guidesConfiguration.getVersion())
                .path(guidesConfiguration.getVersion() + "_" + guide.getSlug() + "_gradle_java_example.zip")
                .toString();
        String gradleTitle = "Gradle " + downloadTitle + " Example";
        return HtmlUtils.link(relativePathToGradleDownload, gradleTitle + " " + HtmlUtils.img("https://graal.cloud/gdk/resources/img/gdk_modules/download-archive.png", "Download completed example", "download-img-guides"));
    }

    @Override
    protected String maven(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        String downloadTitle = capitalizeWords(guide.getSlug().replace("-", " "));
        String relativePathToMavenDownload = UriBuilder.of(BASE_DOWNLOAD_URL)
                .path(guidesConfiguration.getVersion())
                .path(guidesConfiguration.getVersion() + "_" + guide.getSlug() + "_maven_java_example.zip")
                .toString();
        String mavenTitle = "Maven " + downloadTitle + " Example";
        return "<a href=\"" + relativePathToMavenDownload + " \"> " + mavenTitle + " <img class=\"download-img-guides\" src=\"https://graal.cloud/gdk/resources/img/gdk_modules/download-archive.png\" alt=\"Download completed example\"></a>";
    }

    private static String capitalizeWords(String sentence) {
        String[] words = sentence.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;
            String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            result.append(capitalizedWord).append(" ");
        }

        return result.toString().trim();
    }
}

package io.micronaut.guides.cli;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.Cloud;
import io.micronaut.guides.core.Guide;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Serdeable
@JsonSchema
public class GdkGuide extends Guide {
    @JsonPropertyDescription("Set it to true to not generate code samples for the guide")
    @JsonProperty(defaultValue = StringUtils.FALSE)
    @Nullable
    private Boolean skipCodeSamples;

    /**
     * Represents a guide metadata.
     *
     * @param title              The guide's title
     * @param intro              The guide introduction
     * @param authors            The guide's authors
     * @param categories         The guide's categories
     * @param publicationDate    The guide publication date. It should follow the format YYYY-MM-DD
     * @param minimumJavaVersion If the guide needs a minimum Java version, define it here
     * @param maximumJavaVersion If the guide needs a maximum Java version, define it here
     * @param cloud              The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure
     * @param skipGradleTests    Set it to true to skip running the tests for the Gradle applications for the guide
     * @param skipMavenTests     Set it to true to skip running the tests for the Maven applications for the guide
     * @param asciidoctor        The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used
     * @param languages          The guide supported languages
     * @param tags               List of tags added to the guide. features are added automatically as tags. No need to repeat them here
     * @param buildTools         By default, the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here
     * @param testFramework      The guide's test framework. By default, Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock
     * @param zipIncludes        List of additional files with a relative path to include in the generated zip file for the guide
     * @param slug               The guide's slug. If not specified, the guides folder is used
     * @param publish            Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides
     * @param base               Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one
     * @param env                The guide's environment variables
     * @param apps               Applications created for the guide
     * @param skipCodeSamples    Set it to true to not generate code samples for the guide
     */
    @Inject
    public GdkGuide(String title,
                    String intro,
                    List<String> authors,
                    List<String> categories,
                    LocalDate publicationDate,
                    Integer minimumJavaVersion,
                    Integer maximumJavaVersion,
                    Cloud cloud,
                    Boolean skipGradleTests,
                    Boolean skipMavenTests,
                    String asciidoctor,
                    List<Language> languages,
                    List<String> tags,
                    List<BuildTool> buildTools,
                    TestFramework testFramework,
                    List<String> zipIncludes,
                    String slug,
                    Boolean publish,
                    String base,
                    Map<String, String> env,
                    List<GdkApp> apps,
                    Boolean skipCodeSamples) {
        super();
        setTitle(title);
        setIntro(intro);
        setAuthors(authors);
        setCategories(categories);
        setPublicationDate(publicationDate);
        setMinimumJavaVersion(minimumJavaVersion);
        setMaximumJavaVersion(maximumJavaVersion);
        setCloud(cloud);
        setSkipGradleTests(skipGradleTests);
        setSkipMavenTests(skipMavenTests);
        setAsciidoctor(asciidoctor);
        setLanguages(languages);
        setTags(tags);
        setBuildTools(buildTools);
        setTestFramework(testFramework);
        setZipIncludes(zipIncludes);
        setSlug(slug);
        setPublish(publish);
        setBase(base);
        setEnv(env);
        setApps(apps);
        this.skipCodeSamples = skipCodeSamples;
    }

    public @Nullable Boolean skipCodeSamples() {
        return skipCodeSamples;
    }

    public void setSkipCodeSamples(@Nullable Boolean skipCodeSamples) {
        this.skipCodeSamples = skipCodeSamples;
    }
}

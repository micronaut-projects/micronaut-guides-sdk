package io.micronaut.guides.cli;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.guides.core.GuidesTemplatesConfiguration;
import io.micronaut.guides.core.MacroSubstitution;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import jakarta.inject.Singleton;

@Singleton
public class GradleMavenCommandMacro extends GradleMavenTabs implements MacroSubstitution {
    public static final String MACRO = "gradle-maven-command";
    public static final String START = "<code class=\"language-bash hljs\" data-highlighted=\"yes\"  style=\"white-space: pre-line;\">";
    public static final String END = "</code>";
    public static final String ATTRIBUTE_GRADLE = "gradle";
    public static final String ATTRIBUTE_MAVEN = "maven";

    GradleMavenCommandMacro(ResourceLoader resourceLoader,
                            GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        super(resourceLoader, guidesTemplatesConfiguration);
    }

    @Override
    protected String gradle(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        return attributeValue(asciidocMacro, ATTRIBUTE_GRADLE);
    }

    @Override
    protected String maven(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        return attributeValue(asciidocMacro, ATTRIBUTE_MAVEN);
    }

    @Override
    protected String getMacro() {
        return MACRO;
    }

    private String attributeValue(AsciidocMacro asciidocMacro, String attributeKey) {
        Attribute attribute = asciidocMacro.attributes()
                .stream()
                .filter(att -> att.key().equals(attributeKey))
                .findFirst()
                .orElse(null);
        if (attribute == null) {
            return "";
        }
        return START + String.join(" ", attribute.values()) + END;
    }
}
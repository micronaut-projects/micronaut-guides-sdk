package io.micronaut.guides.cli;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.*;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.AsciidocUtils;

abstract class GradleMavenTabs extends LineReplacementMacroSubstitution {
    protected String gradleMavenTabsHtml;

    protected GradleMavenTabs(ResourceLoader resourceLoader, GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        this.gradleMavenTabsHtml = TemplateLoaderUtils.getClasspathTemplate(resourceLoader, guidesTemplatesConfiguration, "gradleMavenTabs.html");
    }

    @NonNull
    protected abstract String gradle(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option);

    @NonNull
    protected abstract String maven(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option);

    @Override
    protected String replacement(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        return AsciidocUtils.passthroughBlock(gradleMavenTabsHtml.replace("{gradle}", gradle(asciidocMacro, guide, option))
                .replace("{maven}", maven(asciidocMacro, guide, option)));
    }
}

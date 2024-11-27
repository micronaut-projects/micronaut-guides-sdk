package io.micronaut.guides.cli;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.*;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.AsciidocUtils;
import io.micronaut.guides.core.asciidoc.Attribute;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

@Singleton
public class EnvironmentVarsMacro extends LineReplacementMacroSubstitution {
    private final static String MACRO = "environment-vars";
    private String html;

    EnvironmentVarsMacro(ResourceLoader resourceLoader, GuidesTemplatesConfiguration guidesTemplatesConfiguration) {
        this.html = TemplateLoaderUtils.getClasspathTemplate(resourceLoader, guidesTemplatesConfiguration, "consoleTabs.html");
    }

    @Override
    protected String getMacro() {
        return MACRO;
    }

    @Override
    protected String replacement(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        StringBuilder bashBuilder = new StringBuilder();
        StringBuilder windowsBuilder = new StringBuilder();
        StringBuilder powershellBuilder = new StringBuilder();

        for (Attribute attribute : asciidocMacro.attributes()) {
            String name = attribute.key();
            String value = attribute.values().get(0);
            bashBuilder.append("<span class=\"hljs-built_in\">export</span> ").append(name).append("=").append(value).append("\n");
            windowsBuilder.append("<span class=\"hljs-built_in\">set</span> ").append(name).append("=").append(value).append("\n");
            powershellBuilder.append("<span class=\"hljs-variable\">$ENV</span> ").append(name).append(" = <span class=\"hljs-string\">\"").append(value).append("\"</span>\n");
        }
        return AsciidocUtils.passthroughBlock(html.replace("{bash}", bashBuilder.toString()).replace("{windows}", windowsBuilder.toString()).replace("{powershell}", powershellBuilder.toString()));
    }
}

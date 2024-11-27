package io.micronaut.guides.cli;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.guides.core.*;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
class CreateAppMacro extends GradleMavenTabs {
    private static final String MACRO = "create-app";
    private static final String SPACE = " ";
    private static final String EQUAL = "=";
    private static final String ARGUMENT_DELIMITER = "--";
    private static final String ARGUMENT_SERVICES = "services";
    private static final String ARGUMENT_FEATURES = "features";
    private static final String ARGUMENT_LANG = "lang";
    private static final String ARGUMENT_BUILD = "build";
    private final GuidesConfiguration guidesConfiguration;

    CreateAppMacro(ResourceLoader resourceLoader,
                             GuidesTemplatesConfiguration guidesTemplatesConfiguration,
                             GuidesConfiguration guidesConfiguration) {
        super(resourceLoader, guidesTemplatesConfiguration);
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public String getMacro() {
        return MACRO;
    }

    @Override
    protected String gradle(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        return command(asciidocMacro, guide, option, BuildTool.GRADLE);
    }

    @Override
    protected String maven(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        return command(asciidocMacro, guide, option, BuildTool.MAVEN);
    }

    private String command(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option, BuildTool buildTool) {
        String target = asciidocMacro.target();
        GdkApp app = gdkApp(guide, target);
        return command(app, option, buildTool);
    }

    private static GdkApp gdkApp(Guide guide, String target) {
        return (GdkApp) guide.getApps().
                stream()
                .filter(a -> a.getName().equals(target))
                .filter(GdkApp.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("App not found: " + target));
    }

    private String command(GdkApp app, GuidesOption option, BuildTool buildTool) {
        return command(guidesConfiguration.getPackageName() + "." + app.getName(),
                new Argument(ARGUMENT_SERVICES, String.join(",", app.getServices())),
                new Argument(ARGUMENT_FEATURES, String.join(",", app.getFeatures())),
                new Argument(ARGUMENT_LANG, option.getLanguage().name().toLowerCase()),
                new Argument(ARGUMENT_BUILD, buildTool.toString()));
    }

    private static String command(Object... parameters) {
        StringBuilder sb = new StringBuilder()
                .append("<code class=\"language-bash hljs\" data-highlighted=\"yes\"  style=\"white-space: pre-line;\">")
                .append("gdk create-app ");
        for (Object p : parameters) {
            if (p instanceof Argument arg) {
                sb.append(argument(arg));
            } else {
                sb.append(SPACE);
                sb.append(p);
            }
            sb.append(" \\\n");
        }
        sb.append("</code>");
        return sb.toString();
    }

    private static String argument(Argument arg) {
        return SPACE +
                ARGUMENT_DELIMITER +
                arg.key() +
                EQUAL +
                arg.value();
    }

    private record Argument(String key, String value) {

    }
}

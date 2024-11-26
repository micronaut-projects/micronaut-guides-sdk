package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.guides.core.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.starter.application.ApplicationType;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.asciidoctor.extension.Name;

import java.net.URI;
import java.util.*;

@Singleton
@Name("diffLink")
public class BuildDiffLinkMacroProcessor extends InlineMacroProcessor {
    private static final String QUERY_PARAMLANG = "lang";
    private static final String QUERY_PARAM_BUILD = "build";
    private static final String QUERY_PARAM_TEST = "test";
    private static final String QUERY_PARAM_NAME = "name";
    private static final String QUERY_PARAM_TYPE = "type";
    private static final String QUERY_PARAM_PACKAGE = "package";
    private static final String QUERY_PARAM_ACTIVITY = "activity";
    private static final String QUERY_PARAM_FEATURES = "features";
    private static final String ATTRIBUTE_FEATURES = "features";
    private static final String ATTRIBUTE_EXCLUDE_FEATURES = "featureExcludes";

    private final GuideContextProvider guideContextProvider;
    private final GuidesConfiguration guidesConfiguration;

    BuildDiffLinkMacroProcessor(GuideContextProvider guideContextProvider, GuidesConfiguration guidesConfiguration) {
        this.guideContextProvider = guideContextProvider;
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public PhraseNode process(StructuralNode parent, String target, Map<String, Object> attributes) {
        String href = buildDiffLink(target, guideContextProvider.getGuide(), guideContextProvider.getOption(), attributes).toString();
        Map<String, Object> options = new HashMap<>();
        options.put("type", ":link");
        options.put("target", href);
        return createPhraseNode(parent, "anchor", target, attributes, options);
    }

    private static Set<String> features(App app, Map<String, Object> attributes, GuidesOption option) {
        Set<String> features = new HashSet<>();
        if (app != null) {
            features.addAll(GuideUtils.getAppVisibleFeatures(app, option.getLanguage()));
        }
        attributes.entrySet().stream()
                .filter(attribute -> attribute.getKey().equals(ATTRIBUTE_FEATURES))
                .map(entry -> (String) entry.getValue())
                .forEach(value -> features.addAll(List.of(value.split(","))));
        attributes.entrySet().stream()
                .filter(attribute -> attribute.getKey().equals(ATTRIBUTE_EXCLUDE_FEATURES))
                .map(entry -> (String) entry.getValue())
                .forEach(value -> features.removeAll(List.of(value.split(","))));

        return features;
    }

    private URI buildDiffLink(String target, Guide guide, GuidesOption option, Map<String, Object> attributes) {
        String appName = target;
        App app = guideContextProvider.getGuide().apps().stream()
                .filter(a -> a.name().equals(target))
                .findFirst()
                .orElse(null);
        Set<String> features = features(app, attributes, option);
        UriBuilder uriBuilder = UriBuilder.of(guidesConfiguration.getProjectGeneratorUrl())
                .queryParam(QUERY_PARAMLANG, option.getLanguage().name())
                .queryParam(QUERY_PARAM_BUILD, option.getBuildTool().name())
                .queryParam(QUERY_PARAM_TEST, option.getTestFramework().name())
                .queryParam(QUERY_PARAM_NAME, appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName)
                .queryParam(QUERY_PARAM_TYPE, app != null ? app.applicationType().name() : ApplicationType.DEFAULT.name())
                .queryParam(QUERY_PARAM_PACKAGE, guidesConfiguration.getPackageName())
                .queryParam(QUERY_PARAM_ACTIVITY, "diff");
        features.forEach(f -> uriBuilder.queryParam(QUERY_PARAM_FEATURES, f));
        return uriBuilder.build();
    }
}

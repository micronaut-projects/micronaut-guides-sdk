/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.starter.application.ApplicationType;
import jakarta.inject.Singleton;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

/**
 * BuildDiffLinkSubstitution is a singleton class that implements the MacroSubstitution interface.
 * It provides methods to substitute macros in Asciidoc files with appropriate values.
 */
@Singleton
public class BuildDiffLinkSubstitution implements MacroSubstitution {
    public static final String MACRO_DIFF_LINK = "diffLink";
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
    private final GuidesConfiguration guidesConfiguration;

    /**
     * Constructs a new BuildDiffLinkSubstitution with the specified guides configuration.
     *
     * @param config the guides configuration
     */
    public BuildDiffLinkSubstitution(GuidesConfiguration config) {
        this.guidesConfiguration = config;
    }

    /**
     * Extracts features from the given application and Asciidoc macro based on the specified guides option.
     *
     * @param app           the application object
     * @param asciidocMacro the Asciidoc macro
     * @param option        the guides option
     * @return a set of features
     */
    private static Set<String> features(App app, AsciidocMacro asciidocMacro, GuidesOption option) {
        Set<String> features = new HashSet<>();
        if (app != null) {
            features.addAll(app.visibleFeatures(option.getLanguage()));
        }
        asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(ATTRIBUTE_FEATURES))
                .map(Attribute::values)
                .forEach(features::addAll);
        asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(ATTRIBUTE_EXCLUDE_FEATURES))
                .map(Attribute::values)
                .forEach(features::removeAll);

        return features;
    }

    /**
     * Substitutes macros in the given string with the appropriate values.
     *
     * @param str    the string containing macros
     * @param guideRender  guide
     * @return the string with macros substituted
     */
    @Override
    public String substitute(String str, GuideRender guideRender) {
        for (String line : findMacroLines(str, MACRO_DIFF_LINK)) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(MACRO_DIFF_LINK, line);
            if (asciidocMacroOptional.isEmpty()) {
                continue;
            }
            AsciidocMacro asciidocMacro = asciidocMacroOptional.get();
            String res = buildDiffLink(asciidocMacro, guideRender).toString() + "[Diff]";
            str = str.replace(line, res);
        }
        return str;
    }

    /**
     * Builds a URI for the diff link based on the given Asciidoc macro, guide, and guides option.
     *
     * @param asciidocMacro the Asciidoc macro
     * @param guideRender the guide object
     * @return the URI for the diff link
     */
    private URI buildDiffLink(AsciidocMacro asciidocMacro, GuideRender guideRender) {
        String appName = appName(asciidocMacro);
        Guide guide = guideRender.guide();
        App app = app(guide, asciidocMacro);
        Set<String> features = features(app, asciidocMacro, guideRender.option());
        UriBuilder uriBuilder = UriBuilder.of(guidesConfiguration.getProjectGeneratorUrl())
                .queryParam(QUERY_PARAMLANG, guideRender.option().getLanguage().name())
                .queryParam(QUERY_PARAM_BUILD, guideRender.option().getBuildTool().name())
                .queryParam(QUERY_PARAM_TEST, guideRender.option().getTestFramework().name())
                .queryParam(QUERY_PARAM_NAME, appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName)
                .queryParam(QUERY_PARAM_TYPE, app != null ? app.getApplicationType().name() : ApplicationType.DEFAULT.name())
                .queryParam(QUERY_PARAM_PACKAGE, guidesConfiguration.getPackageName())
                .queryParam(QUERY_PARAM_ACTIVITY, "diff");
        features.forEach(f -> uriBuilder.queryParam(QUERY_PARAM_FEATURES, f));
        return uriBuilder.build();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

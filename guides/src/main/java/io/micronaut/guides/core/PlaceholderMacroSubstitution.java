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

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Map;

import static io.micronaut.starter.api.TestFramework.SPOCK;

/**
 * Class that provides macro substitution functionality for placeholders in guide templates.
 */
@Singleton
public class PlaceholderMacroSubstitution implements MacroSubstitution {

    private final GuidesConfiguration guidesConfiguration;
    private final CoordinatesProvider coordinatesProvider;

    /**
     * Constructs a new PlaceholderMacroSubstitution.
     *
     * @param guidesConfiguration the guides configuration
     * @param coordinatesProvider the coordinates provider
     */
    public PlaceholderMacroSubstitution(GuidesConfiguration guidesConfiguration, CoordinatesProvider coordinatesProvider) {
        this.guidesConfiguration = guidesConfiguration;
        this.coordinatesProvider = coordinatesProvider;
    }

    /**
     * Substitutes placeholders in the given string with values from the guide and option.
     *
     * @param str    the string containing placeholders
     * @param guide  the guide metadata
     * @param option the guide option
     * @return the string with placeholders substituted
     */
    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {

        str = str.replace("{githubSlug}", guide.getSlug());
        str = str.replace("@language@", StringUtils.capitalize(option.getLanguage().toString()));
        str = str.replace("@guideTitle@", guide.getTitle());
        str = str.replace("@guideIntro@", guide.getIntro());
        str = str.replace("@micronaut@", String.valueOf(guidesConfiguration.getVersion()));
        str = str.replace("@lang@", option.getLanguage().toString());
        str = str.replace("@build@", option.getBuildTool().toString());
        str = str.replace("@testFramework@", option.getTestFramework().toString());
        str = str.replace("@authors@", String.join(", ", guide.getAuthors()));
        str = str.replace("@languageextension@", option.getLanguage().getExtension());
        str = str.replace("@testsuffix@", option.getTestFramework() == SPOCK ? "Spec" : "Test");
        str = str.replace("@sourceDir@", MacroUtils.getSourceDir(guide.getSlug(), option));
        str = str.replace("@minJdk@", String.valueOf(guide.getMinimumJavaVersion() != null ? guide.getMinimumJavaVersion() : guidesConfiguration.getDefaultMinJdk()));
        str = str.replace("@api@", guidesConfiguration.getApiUrl());

        for (Map.Entry<String, Coordinate> entry : coordinatesProvider.getCoordinates().entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue().getVersion())) {
                str = str.replace("@" + entry.getKey() + "Version@", entry.getValue().getVersion());
            }
        }

        str = str.replace("@micronautVersion@", VersionInfo.getMicronautVersion());

        return str;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

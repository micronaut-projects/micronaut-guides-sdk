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
package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.Internal;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideRender;
import io.micronaut.guides.core.GuidesOption;
import jakarta.inject.Singleton;

import java.util.*;

@Internal
@Singleton
class DefaultGuideRenderAttributesProvider implements GuideRenderAttributesProvider {
    private static final String ATTRIBUTE_CLOUD = "cloud";
    private static final String ATTRIBUTE_LANGUAGE = "language";
    private static final String ATTRIBUTE_TEST_FRAMEWORK = "testFramework";
    private static final String ATTRIBUTE_BUILD_TOOL = "buildTool";
    private static final String ATTRIBUTE_INTRO = "intro";
    private static final String ATTRIBUTE_TITLE = "title";

    @Override
    public Map<String, Object> attributes(GuideRender guideRender) {
        Map<String, Object> attributes = new HashMap<>();
        Guide guide = guideRender.guide();
        GuidesOption guidesOption = guideRender.option();
        if (guide.getCloud() != null) {
            attributes.put(ATTRIBUTE_CLOUD, guide.getCloud().getAccronym());
        }
        attributes.put(ATTRIBUTE_LANGUAGE, guidesOption.getLanguage());
        attributes.put(ATTRIBUTE_TEST_FRAMEWORK, guidesOption.getTestFramework());
        attributes.put(ATTRIBUTE_BUILD_TOOL, guidesOption.getBuildTool());
        attributes.put(ATTRIBUTE_INTRO, guide.getIntro());
        attributes.put(ATTRIBUTE_TITLE, guide.getTitle());
        return attributes;
    }
}

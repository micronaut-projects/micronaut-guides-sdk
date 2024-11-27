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
package io.micronaut.guides.core.html;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideGenerationUtils;
import io.micronaut.guides.core.GuidesOption;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Default implementation of the {@link GuideMatrixGenerator} interface.
 * This class is responsible for generating a matrix index for guides.
 */
@Singleton
class DefaultGuideMatrixGenerator implements GuideMatrixGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideMatrixGenerator.class);

    @Override
    public String renderIndex(Guide guide) {
        StringBuilder sb = new StringBuilder();
        List<GuidesOption> guideOptions = GuideGenerationUtils.guidesOptions(guide, LOG);
        sb.append("<!DOCTYPE html><html><head></head><body>");
        sb.append("<h1>");
        sb.append(guide.getTitle());
        sb.append("</h1>");
        sb.append("<ul>");
        for (GuidesOption guideOption : guideOptions) {
            String href = guide.getSlug() + "-" + guideOption.getBuildTool() + "-" + guideOption.getLanguage() + ".html";
            String title = guideOption.getBuildTool() + " " + guideOption.getLanguage();
            sb.append("<li>");
            sb.append("<a href=\"");
            sb.append(href);
            sb.append("\">");
            sb.append(title);
            sb.append("</a>");
            sb.append("</li>");
        }
        sb.append("</ul>");
        sb.append("</body></html>");
        return sb.toString();
    }
}

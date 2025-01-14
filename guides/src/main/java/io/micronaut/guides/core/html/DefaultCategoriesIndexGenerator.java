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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesConfiguration;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Default implementation of {@link CategoriesIndexGenerator}.
 */
@Singleton
public class DefaultCategoriesIndexGenerator implements CategoriesIndexGenerator {

    private final GuidesConfiguration guidesConfiguration;

    DefaultCategoriesIndexGenerator(GuidesConfiguration guidesConfiguration) {
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public String renderIndex(List<? extends Guide> guides) {
        String content = "";
        if (StringUtils.isNotEmpty(guidesConfiguration.getTitle())) {
            content += "<h1>" + guidesConfiguration.getTitle() + "<h1>";
        }
        content += guidesContent(guides);
        return HtmlUtils.html5(guidesConfiguration.getTitle(), content);
    }

    /**
     * @param guides Guides
     * @return HTML content for the guides list
     */
    protected String guidesContent(@NonNull List<? extends Guide> guides) {
        StringBuilder sb = new StringBuilder();
        List<String> modules = guides.stream()
                .flatMap(g -> g.getCategories().stream())
                .distinct()
                .toList();
        for (String module : modules) {
            sb.append("<h2>");
            sb.append(module);
            sb.append("</h2>");
            sb.append("<ul>");
            for (Guide guide : guides) {
                if (guide.getCategories().contains(module)) {
                    sb.append(guideContent(guide));
                }
            }
            sb.append("</ul>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * @param guide Guide
     * @return HTML content for an individual guide
     */
    protected String guideContent(@NonNull Guide guide) {
        StringBuilder sb = new StringBuilder();
        String href = guide.getSlug() + ".html";
        String title = guide.getTitle();
        sb.append("<li>");
        sb.append("<a href=\"");
        sb.append(href);
        sb.append("\">");
        sb.append(title);
        sb.append("</a>");
        sb.append("</li>");
        return sb.toString();
    }
}

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

/**
 * Utility class for HTML.
 */
public final class HtmlUtils {
    private HtmlUtils() {
    }

    public static String img(String src, String alt, String... classes) {
        StringBuilder img = new StringBuilder("<img src=\"").append(src).append("\" alt=\"").append(alt).append("\"");
        if (classes.length > 0) {
            img.append(" class=\"");
            for (String clazz : classes) {
                img.append(clazz).append(" ");
            }
            img.deleteCharAt(img.length() - 1);
            img.append("\"");
        }
        img.append("/>");
        return img.toString();
    }

    /**
     *
     * @param href link url
     * @param text link text
     * @return HTML Link
     */
    public static String link(String href, String text) {
        return "<a href=\"" + href + "\">" + text + "</a>";
    }
}

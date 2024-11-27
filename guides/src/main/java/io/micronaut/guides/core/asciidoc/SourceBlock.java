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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * GuideUtils is a utility class that provides various helper methods for handling guides.
 * It contains methods for retrieving tags, features, frameworks, and merging guide data.
 * This class cannot be instantiated.
 */
public class SourceBlock {
    private static final String SEPARATOR = "----";

    @Nullable
    private final String title;

    @Nullable
    private final List<IncludeDirective> includeDirectives;

    @Nullable
    private final String language;

    @Nullable
    private final String content;

    /**
     * Constructs a new SourceBlock.
     *
     * @param title             the title of the source block
     * @param content the content of the source block
     * @param includeDirectives the list of include directives
     * @param language          the language of the source block
     */
    public SourceBlock(String title,
                       String content,
                       List<IncludeDirective> includeDirectives,
                       String language) {
        this.title = title;
        this.content = content;
        this.includeDirectives = includeDirectives;
        this.language = language;
    }

    /**
     * Gets the title of the source block.
     *
     * @return the title, or null if not set
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     * Gets the list of include directives.
     *
     * @return the list of include directives, or null if not set
     */
    @Nullable
    public List<IncludeDirective> getIncludeDirectives() {
        return includeDirectives;
    }

    /**
     * Gets the language of the source block.
     *
     * @return the language, or null if not set
     */
    @Nullable
    public String getLanguage() {
        return language;
    }

    /**
     * Returns a string representation of the source block.
     *
     * @return the string representation of the source block
     */
    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        lines.add("[source," + getLanguage() + "]");
        if (getTitle() != null) {
            lines.add("." + getTitle());
        }
        lines.add(SEPARATOR);
        if (StringUtils.isNotEmpty(content)) {
            lines.add(content);
        }
        if (getIncludeDirectives() != null) {
            getIncludeDirectives().stream()
                    .map(IncludeDirective::toString)
                    .forEach(lines::add);
        }
        lines.add(SEPARATOR);
        return String.join("\n", lines);
    }

    /**
     * Creates a new Builder instance.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing SourceBlock instances.
     */
    public static class Builder {
        @Nullable
        private String title;

        @Nullable
        private List<IncludeDirective> includeDirectives;

        @Nullable
        private String language;

        @Nullable
        private String content;

        /**
         * Sets the title of the source block.
         *
         * @param title the title to set
         * @return the builder instance
         */
        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the content of the source block.
         *
         * @param content source block content
         * @return the builder instance
         */
        public Builder content(@Nullable String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets the list of include directives.
         *
         * @param includeDirectives the list of include directives to set
         * @return the builder instance
         */
        public Builder includeDirectives(@Nullable List<IncludeDirective> includeDirectives) {
            this.includeDirectives = includeDirectives;
            return this;
        }

        /**
         * Adds an include directive to the list.
         *
         * @param includeDirective the include directive to add
         * @return the builder instance
         */
        public Builder includeDirective(@Nullable IncludeDirective includeDirective) {
            if (this.includeDirectives == null) {
                this.includeDirectives = new ArrayList<>();
            }
            this.includeDirectives.add(includeDirective);
            return this;
        }

        /**
         * Sets the language of the source block.
         *
         * @param language the language to set
         * @return the builder instance
         */
        public Builder language(@Nullable String language) {
            this.language = language;
            return this;
        }

        /**
         * Builds a new SourceBlock instance.
         *
         * @return the built SourceBlock instance
         */
        public SourceBlock build() {
            return new SourceBlock(title, content, includeDirectives, language);
        }
    }
}

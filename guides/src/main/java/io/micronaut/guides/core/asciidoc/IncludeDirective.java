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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Asciidoc Include Directive.
 * <a href="https://asciidoctor.org/docs/user-manual/#include-directive">Include Directive</a>
 */
public class IncludeDirective {

    public static final String ATTRIBUTE_LEVELOFFSET = "leveloffset";
    public static final String ATTRIBUTE_LINES = "lines";
    public static final String ATTRIBUTE_ENCODING = "encoding";
    public static final String ATTRIBUTE_TAG = "tag";
    public static final String ATTRIBUTE_TAGS = ATTRIBUTE_TAG + "s";
    public static final String ATTRIBUTE_INDENT = "indent";
    public static final String ATTRIBUTE_OPTS = "opts";
    /**
     * Target may be an absolute path, a path relative to the current document, or a URL.
     */
    @NonNull
    private final String target;

    /**
     * @see <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-leveloffset/">Level Offset</a>.
     */
    @Nullable
    private final String levelOffset;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-lines/">Include Content by Line Ranges</a>.
     * ranges of line numbers.
     */
    @Nullable
    private final Range lines;

    @Nullable
    private final String encoding;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-tagged-regions/">Included Tagged Regions</a>.
     */
    @Nullable
    private final List<String> tags;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-indent/">Include with Ident</a>.
     */
    @Nullable
    private final Integer indent;

    @Nullable
    private final String opts;

    IncludeDirective(@NonNull String target, @Nullable String levelOffset, @Nullable Range lines, @Nullable String encoding, @Nullable List<String> tags, @Nullable Integer indent, @Nullable String opts) {
        this.target = target;
        this.levelOffset = levelOffset;
        this.lines = lines;
        this.encoding = encoding;
        this.tags = tags;
        this.indent = indent;
        this.opts = opts;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the target.
     *
     * @return Target may be an absolute path, a path relative to the current document, or a URL.
     */
    @NonNull
    public String getTarget() {
        return target;
    }

    /**
     * Gets the level offset.
     *
     * @return the level offset.
     * @see <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-leveloffset/">Level Offset</a>
     */
    public @Nullable String getLevelOffset() {
        return levelOffset;
    }

    /**
     * Gets the line ranges.
     *
     * @return ranges of line numbers.
     */
    @Nullable
    public Range getLines() {
        return lines;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding, or null if not set
     */
    @Nullable
    public String getEncoding() {
        return encoding;
    }

    /**
     * Gets the tags.
     *
     * @return the list of tags, or null if not set
     */
    @Nullable
    public List<String> getTags() {
        return tags;
    }

    /**
     * Gets the indent.
     *
     * @return the indent value, or null if not set
     */
    @Nullable
    public Integer getIndent() {
        return indent;
    }

    /**
     * Gets the options.
     *
     * @return the options, or null if not set
     */
    @Nullable
    public String getOpts() {
        return opts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("include::").append(getTarget()).append('[');
        List<String> attributes = attributes();
        if (CollectionUtils.isNotEmpty(attributes)) {
            sb.append(String.join(",", attributes));
        }
        sb.append("]");
        return sb.toString();
    }

    private List<String> attributes() {
        List<String> attributes = new ArrayList<>();
        if (getLevelOffset() != null) {
            attributes.add(ATTRIBUTE_LEVELOFFSET + "=" + getLevelOffset());
        }
        if (getLines() != null && getLines().isValid()) {
            attributes.add(ATTRIBUTE_LINES + "=" + getLines().from() + ".." + getLines().to());
        }
        if (getEncoding() != null) {
            attributes.add(ATTRIBUTE_ENCODING + "=" + getEncoding());
        }
        if (CollectionUtils.isNotEmpty(getTags())) {
            if (getTags().size() > 1) {
                attributes.add(ATTRIBUTE_TAGS + "=" + String.join(";", getTags()));
            } else if (getTags().size() == 1) {
                attributes.add(ATTRIBUTE_TAG + "=" + getTags().get(0));
            }
        }
        if (getIndent() != null) {
            attributes.add(ATTRIBUTE_INDENT + "=" + getIndent());
        }
        if (getOpts() != null) {
            attributes.add(ATTRIBUTE_OPTS + "=" + getOpts());
        }
        return attributes;
    }

    /**
     * Builder class for constructing IncludeDirective instances.
     */
    public static class Builder {
        @NonNull
        private String target;

        @Nullable
        private String levelOffset;

        @Nullable
        private Range lines;

        @Nullable
        private String encoding;

        @Nullable
        private List<String> tags;

        @Nullable
        private Integer indent;

        @Nullable
        private String opts;

        /**
         * Sets the target.
         *
         * @param target the target to set
         * @return the builder instance
         */
        public Builder target(@NonNull String target) {
            this.target = target;
            return this;
        }

        /**
         * Sets the level offset.
         *
         * @param levelOffset the level offset to set
         * @return the builder instance
         */
        public Builder levelOffset(@Nullable String levelOffset) {
            this.levelOffset = levelOffset;
            return this;
        }

        /**
         * Sets the line ranges.
         *
         * @param lines the line ranges to set
         * @return the builder instance
         */
        public Builder lines(@Nullable Range lines) {
            this.lines = lines;
            return this;
        }

        /**
         * Sets the encoding.
         *
         * @param encoding the encoding to set
         * @return the builder instance
         */
        public Builder encoding(@Nullable String encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * Sets the tags.
         *
         * @param tags the tags to set
         * @return the builder instance
         */
        public Builder tags(@Nullable List<String> tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Sets the indent.
         *
         * @param indent the indent to set
         * @return the builder instance
         */
        public Builder indent(@Nullable Integer indent) {
            this.indent = indent;
            return this;
        }

        /**
         * Sets the options.
         *
         * @param opts the options to set
         * @return the builder instance
         */
        public Builder opts(@Nullable String opts) {
            this.opts = opts;
            return this;
        }

        /**
         * Sets the level offset.
         *
         * @param levelOffset the level offset to set
         * @return the builder instance
         */
        public Builder levelOffset(int levelOffset) {
            return levelOffset("+" + levelOffset);
        }

        /**
         * Adds a tag.
         *
         * @param tag the tag to add
         * @return the builder instance
         */
        public Builder tag(String tag) {
            if (tags == null) {
                tags = new ArrayList<>();
            }
            tags.add(tag);
            return this;
        }

        /**
         * Builds the IncludeDirective instance.
         *
         * @return the built IncludeDirective instance
         */
        public IncludeDirective build() {
            return new IncludeDirective(Objects.requireNonNull(target), levelOffset, lines, encoding, tags, indent, opts);
        }

        /**
         * Sets the attributes.
         *
         * @param attributes the attributes to set
         * @return the builder instance
         */
        public Builder attributes(List<Attribute> attributes) {
            for (Attribute attribute : attributes) {
                switch (attribute.key()) {
                    case ATTRIBUTE_LEVELOFFSET:
                        for (String value : attribute.values()) {
                            levelOffset(value);
                        }
                        break;
                    case ATTRIBUTE_LINES:
                        for (String value : attribute.values()) {
                            String[] arr = value.split("\\.\\.");
                            if (arr.length == 2) {
                                lines(new Range(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
                            }
                        }
                        break;
                    case ATTRIBUTE_ENCODING:
                        for (String value : attribute.values()) {
                            encoding(value);
                        }
                        break;
                    case ATTRIBUTE_TAGS, ATTRIBUTE_TAG:
                        for (String value : attribute.values()) {
                            tag(value);
                        }
                        break;
                    case ATTRIBUTE_INDENT:
                        for (String value : attribute.values()) {
                            indent(Integer.valueOf(value));
                        }
                        break;
                    case ATTRIBUTE_OPTS:
                        for (String value : attribute.values()) {
                            opts(value);
                        }
                        break;
                    default:
                        break;
                }
            }

            return this;
        }
    }
}

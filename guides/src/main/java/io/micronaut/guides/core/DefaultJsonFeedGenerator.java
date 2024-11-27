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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.json.JsonMapper;
import io.micronaut.rss.jsonfeed.JsonFeed;
import io.micronaut.rss.jsonfeed.JsonFeedAuthor;
import io.micronaut.rss.jsonfeed.JsonFeedItem;
import io.micronaut.rss.language.RssLanguage;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Class that implements the JsonFeedGenerator interface.
 */
@Singleton
public class DefaultJsonFeedGenerator implements JsonFeedGenerator {
    private final GuidesConfiguration guidesConfiguration;
    private final JsonFeedConfiguration jsonFeedConfiguration;
    private final JsonMapper jsonMapper;

    /**
     * Constructs a new DefaultJsonFeedGenerator.
     *
     * @param guidesConfiguration   the configuration for guides
     * @param jsonFeedConfiguration the configuration for JSON feed
     * @param jsonMapper            the JSON mapper
     */
    public DefaultJsonFeedGenerator(GuidesConfiguration guidesConfiguration, JsonFeedConfiguration jsonFeedConfiguration, JsonMapper jsonMapper) {
        this.guidesConfiguration = guidesConfiguration;
        this.jsonFeedConfiguration = jsonFeedConfiguration;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Generates a JsonFeed from the provided list of guide metadata.
     *
     * @param metadatas the list of guide metadata
     * @return the generated JsonFeed
     */
    public JsonFeed jsonFeed(List<? extends Guide> metadatas) {
        JsonFeed.Builder jsonFeedBuilder = jsonFeedBuilder();
        for (Guide metadata : metadatas) {
            jsonFeedBuilder.item(jsonFeedItem(metadata));
        }
        return jsonFeedBuilder.build();
    }

    /**
     * Generates a JSON string representation of the JsonFeed from the provided list of guide metadata.
     *
     * @param metadatas the list of guide metadata
     * @return the JSON string representation of the JsonFeed
     * @throws IOException if an I/O error occurs during JSON serialization
     */
    @Override
    @NonNull
    public String jsonFeedString(@NonNull List<? extends Guide> metadatas) throws IOException {
        JsonFeed jsonFeed = jsonFeed(metadatas);
        return jsonMapper.writeValueAsString(jsonFeed);
    }

    private JsonFeed.Builder jsonFeedBuilder() {
        return JsonFeed.builder()
                .version(JsonFeed.VERSION_JSON_FEED_1_1)
                .title(guidesConfiguration.getTitle())
                .homePageUrl(guidesConfiguration.getHomePageUrl())
                .feedUrl(jsonFeedConfiguration.getFeedUrl());
    }

    private JsonFeedItem jsonFeedItem(Guide metadata) {
        JsonFeedItem.Builder jsonFeedItemBuilder = JsonFeedItem.builder()
                .id(metadata.getSlug())
                .title(metadata.getTitle())
                .contentText(metadata.getIntro())
                .language(RssLanguage.LANG_ENGLISH)
                .datePublished(ZonedDateTime.of(metadata.getPublicationDate(), LocalTime.of(0, 0), ZoneOffset.UTC))
                .url(guidesConfiguration.getHomePageUrl() + metadata.getSlug());
        for (String author : metadata.getAuthors()) {
            jsonFeedItemBuilder.author(JsonFeedAuthor.builder().name(author).build());
        }
        for (String t : metadata.getTags()) {
            jsonFeedItemBuilder.tag(t);
        }
        return jsonFeedItemBuilder.build();
    }
}

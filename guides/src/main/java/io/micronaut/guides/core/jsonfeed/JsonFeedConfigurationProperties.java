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
package io.micronaut.guides.core.jsonfeed;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.guides.core.GuidesConfigurationProperties;

/**
 * Configuration properties for JSON feed.
 * This class implements the JsonFeedConfiguration interface and provides
 * configuration properties for the JSON feed.
 */
@ConfigurationProperties(GuidesConfigurationProperties.PREFIX + ".json-feed")
public class JsonFeedConfigurationProperties implements JsonFeedConfiguration {
    private static final String JSON_FEED_FILENAME = "feed.json";
    private String feedUrl = GuidesConfigurationProperties.GUIDES_URL + JSON_FEED_FILENAME;
    private String filename = JSON_FEED_FILENAME;

    @Override
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename for the JSON feed.
     *
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String getFeedUrl() {
        return feedUrl;
    }

    /**
     * Sets the feed URL.
     *
     * @param feedUrl the feed URL to set
     */
    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }
}

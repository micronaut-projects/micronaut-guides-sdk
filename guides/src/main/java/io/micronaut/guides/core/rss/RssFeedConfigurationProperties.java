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
package io.micronaut.guides.core.rss;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.guides.core.GuidesConfigurationProperties;

/**
 * RssFeedConfigurationProperties is a configuration properties class that implements the RssFeedConfiguration interface.
 * It is used to configure the RSS feed properties.
 */
@ConfigurationProperties(GuidesConfigurationProperties.PREFIX + ".rss-feed")
public class RssFeedConfigurationProperties implements RssFeedConfiguration {
    private static final String JSON_FEED_FILENAME = "rss.xml";
    private String filename = JSON_FEED_FILENAME;

    /**
     * Returns the filename for the RSS feed.
     *
     * @return the filename for the RSS feed
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename for the RSS feed.
     *
     * @param filename the filename to be set for the RSS feed
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}

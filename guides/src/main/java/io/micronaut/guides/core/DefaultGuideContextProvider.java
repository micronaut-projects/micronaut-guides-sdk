package io.micronaut.guides.core;

import jakarta.inject.Singleton;

/**
 * Default implementation of the GuideContextProvider interface.
 */
@Singleton
public class DefaultGuideContextProvider implements GuideContextProvider {
    private Guide guide;
    private GuidesOption option;

    /**
     * Gets the guide.
     *
     * @return The guide.
     */
    @Override
    public Guide getGuide() {
        return guide;
    }

    /**
     * Gets the guide option.
     *
     * @return The guide option.
     */
    @Override
    public GuidesOption getOption() {
        return option;
    }

    /**
     * Sets the guide.
     *
     * @param guide The guide to set.
     */
    @Override
    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    /**
     * Sets the guide option.
     *
     * @param option The guide option to set.
     */
    @Override
    public void setOption(GuidesOption option) {
        this.option = option;
    }
}

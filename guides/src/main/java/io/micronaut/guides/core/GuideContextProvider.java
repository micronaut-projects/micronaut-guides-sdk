package io.micronaut.guides.core;

/**
 * Interface for providing guide context.
 */
public interface GuideContextProvider {

    /**
     * Gets the guide.
     *
     * @return The guide.
     */
    Guide getGuide();

    /**
     * Gets the guide option.
     *
     * @return The guide option.
     */
    GuidesOption getOption();

    /**
     * Gets the working directory.
     *
     * @return The working directory.
     */
    String getBaseDir();

    /**
     * Sets the guide.
     *
     * @param guide The guide to set.
     */
    void setGuide(Guide guide);

    /**
     * Sets the guide option.
     *
     * @param option The guide option to set.
     */
    void setOption(GuidesOption option);

    /**
     * Sets the working directory.
     *
     * @param workingDir The working directory to set.
     */
    void setBaseDir(String workingDir);
}

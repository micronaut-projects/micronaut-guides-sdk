package io.micronaut.guides.core.html;

import io.micronaut.guides.core.Guide;

import java.util.List;

public interface TocGenerator {
    /**
     * Generate a Table of Contents for the given guide.
     *
     * @param guide     The guide
     * @param guides    The list of guides
     * @param guideHtml The guide HTML
     * @return A list containing the TOC, which is always the first element, and the substitution to be made in the guide HTML
     */
    List<String> generateToc(Guide guide, List<? extends Guide> guides, String guideHtml);
}

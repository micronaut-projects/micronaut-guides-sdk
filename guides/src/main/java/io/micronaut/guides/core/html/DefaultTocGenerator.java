package io.micronaut.guides.core.html;

import io.micronaut.guides.core.Guide;

import java.util.ArrayList;
import java.util.List;

public class DefaultTocGenerator implements TocGenerator {

    @Override
    public List<String> generateToc(Guide guide, List<? extends Guide> guides, String guideHtml) {
        List<String> extractedToc = extractToc(guideHtml);
        String tocHtml;

        if (extractedToc.isEmpty()) {
            tocHtml = "";
        } else {
            tocHtml = extractedToc.get(0);
        }
        extractedToc.add(0, tocHtml);

        return extractedToc;
    }

    /**
     * Extracts the table of contents (TOC) from the given HTML content.
     *
     * @param html The HTML content as a string.
     * @return A list of strings representing the TOC div elements found in the HTML.
     */
    protected List<String> extractToc(String html) {
        List<String> tocDivs = new ArrayList<>();
        String openDivPattern = "<div";
        String closeDivPattern = "</div>";
        String classAttribute = "class=\"toc-floating\"";
        String idAttribute = "id=\"toc\"";

        int startIndex = html.indexOf(openDivPattern + " " + classAttribute);
        if (startIndex != -1) {
            int openingTagEnd = html.indexOf(">", startIndex);
            if (openingTagEnd != -1) {
                int nestedDivCount = 0;
                int currentIndex = openingTagEnd + 1;

                while (currentIndex < html.length()) {
                    int nextOpenDiv = html.indexOf(openDivPattern, currentIndex);
                    int nextCloseDiv = html.indexOf(closeDivPattern, currentIndex);

                    if (nextCloseDiv == -1) {
                        break;
                    }

                    if (nextOpenDiv != -1 && nextOpenDiv < nextCloseDiv) {
                        nestedDivCount++;
                        currentIndex = nextOpenDiv + openDivPattern.length();
                    } else {
                        if (nestedDivCount == 0) {
                            tocDivs.add(html.substring(startIndex, nextCloseDiv + closeDivPattern.length()));
                            break;
                        }
                        nestedDivCount--;
                        currentIndex = nextCloseDiv + closeDivPattern.length();
                    }
                }
            }
        }

        startIndex = html.indexOf(openDivPattern + " " + idAttribute);
        if (startIndex == -1) {
            startIndex = html.indexOf(openDivPattern + " id='toc'");
        }

        if (startIndex != -1) {
            int openingTagEnd = html.indexOf(">", startIndex);
            if (openingTagEnd != -1) {
                int nestedDivCount = 0;
                int currentIndex = openingTagEnd + 1;

                while (currentIndex < html.length()) {
                    int nextOpenDiv = html.indexOf(openDivPattern, currentIndex);
                    int nextCloseDiv = html.indexOf(closeDivPattern, currentIndex);

                    if (nextCloseDiv == -1) {
                        break;
                    }

                    if (nextOpenDiv != -1 && nextOpenDiv < nextCloseDiv) {
                        nestedDivCount++;
                        currentIndex = nextOpenDiv + openDivPattern.length();
                    } else {
                        if (nestedDivCount == 0) {
                            tocDivs.add(html.substring(startIndex, nextCloseDiv + closeDivPattern.length()));
                            break;
                        }
                        nestedDivCount--;
                        currentIndex = nextCloseDiv + closeDivPattern.length();
                    }
                }
            }
        }

        return tocDivs;
    }
}

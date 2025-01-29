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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MacroUtils is a utility class that provides various methods for handling macros in strings.
 */
public final class MacroUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private MacroUtils() { }

    /**
     * Returns the source directory name based on the provided slug and GuidesOption.
     *
     * @param slug   the slug
     * @param option the guides option
     * @return the source directory name
     */
    @NonNull
    public static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    /**
     * Finds lines in the provided string that start with the specified macro.
     *
     * @param str   the string to search
     * @param macro the macro to find
     * @return a list of lines that start with the specified macro
     */
    public static List<String> findMacroLines(@NonNull String str, @NonNull String macro) {
        return str.lines().filter(line -> line.startsWith(macro + ":")).toList();
    }

    /**
     * Finds instances of the specified pattern in the provided string.
     *
     * @param str     the string to search
     * @param pattern the pattern to find
     * @return a list of matches found in the string
     */
    static List<String> findMacroInstances(@NonNull String str, @NonNull Pattern pattern) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

    /**
     * Finds groups of macros in the provided string.
     *
     * @param str   the string to search
     * @param macro the macro to find
     * @return a list of macro groups found in the string
     */
    static List<String> findMacroGroups(@NonNull String str, @NonNull String macro) {
        List<String> matches = new ArrayList<>();
        String pattern = ":" + macro + ":";
        int startIndex = 0;

        while (true) {
            startIndex = str.indexOf(pattern, startIndex);
            if (startIndex == -1) {
                break;
            }

            int endIndex = str.indexOf(pattern, startIndex + pattern.length());
            if (endIndex == -1) {
                break;
            }

            String match = str.substring(startIndex, endIndex + pattern.length());
            matches.add(match);

            startIndex = endIndex + pattern.length();
        }

        return matches;
    }

    /**
     * Extracts parameters from a macro group line.
     *
     * @param line  the macro group line
     * @param macro the macro to extract parameters from
     * @return a list of parameters extracted from the macro group line
     */
    @NonNull
    static List<String> extractMacroGroupParameters(@NonNull String line, @NonNull String macro) {
        return Arrays.stream(line.substring(macro.length() + 2).split(",")).filter(el -> !el.isEmpty()).toList();
    }

    /**
     * Finds nested macro groups in the provided string.
     *
     * @param str   the string to search
     * @param macro the macro to find
     * @return a list of nested macro groups found in the string
     */
    @NonNull
    static List<List<String>> findMacroGroupsNested(@NonNull String str, @NonNull String macro) {
        List<List<String>> matches = new ArrayList<>();
        String pattern = ":" + macro + ":";

        List<String> lines = str.lines().toList();
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (isGroupStart(line, pattern)) {
                stack.push(i);
            } else if (isGroupEnd(line, pattern)) {
                if (!stack.isEmpty()) {
                    int start = stack.pop();
                    matches.add(lines.subList(start, i + 1));
                } else {
                    throw new UnsupportedOperationException("Unbalanced macro group");
                }
            }
        }

        return matches;
    }

    /**
     * Checks if the line is the start of a macro group.
     *
     * @param line  the line to check
     * @param macro the macro to check for
     * @return true if the line is the start of a macro group, false otherwise
     */
    private static boolean isGroupStart(@NonNull String line, @NonNull String macro) {
        return line.matches(macro + "[a-zA-Z0-9,]+");
    }

    /**
     * Checks if the line is the end of a macro group.
     *
     * @param line  the line to check
     * @param macro the macro to check for
     * @return true if the line is the end of a macro group, false otherwise
     */
    private static boolean isGroupEnd(@NonNull String line, @NonNull String macro) {
        return line.matches(macro + "(?![a-zA-Z0-9,]+$)");
    }

    /**
     * Resolves the Asciidoctor language based on the file extension.
     *
     * @param extension the file extension
     * @return the resolved Asciidoctor language
     */
    @NonNull
    static String resolveAsciidoctorLanguage(@NonNull String extension) {
        return switch (extension.toLowerCase()) {
            case "yml", "yaml" -> "yaml";
            case "html", "vm", "hbs" -> "html";
            case "xml" -> "xml";
            default -> extension.toLowerCase();
        };
    }
}

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
import io.micronaut.guides.core.asciidoc.*;

import java.nio.file.Path;
import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.guides.core.asciidoc.IncludeDirective.ATTRIBUTE_LINES;

abstract class SourceBlockMacroSubstitution implements MacroSubstitution {

    private final LicenseLoader licenseLoader;
    private final GuidesConfiguration guidesConfiguration;

    SourceBlockMacroSubstitution(LicenseLoader licenseLoader,
                                 GuidesConfiguration guidesConfiguration) {
        this.licenseLoader = licenseLoader;
        this.guidesConfiguration = guidesConfiguration;
    }

    /**
     * Retrieves the name of the macro.
     *
     * @return The name of the macro.
     */
    protected abstract String getMacroName();

    /**
     * Retrieves the classpath for the macro.
     *
     * @return The classpath for the macro.
     */
    protected abstract Classpath getClasspath();

    /**
     * Retrieves the file type for the macro.
     *
     * @return The file type for the macro.
     */
    protected abstract FileType getFileType();

    /**
     * Retrieves the LicenseLoader instance.
     *
     * @return The LicenseLoader instance.
     */
    public LicenseLoader getLicenseLoader() {
        return licenseLoader;
    }

    /**
     * Retrieves the GuidesConfiguration instance.
     *
     * @return The GuidesConfiguration instance.
     */
    public GuidesConfiguration getGuidesConfiguration() {
        return guidesConfiguration;
    }

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        String slug = guide.getSlug();
        for (String line : findMacroLines(str, getMacroName())) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(getMacroName(), line);
            if (asciidocMacroOptional.isPresent()) {
                AsciidocMacro asciidocMacro = asciidocMacroOptional.get();
                String appName = appName(asciidocMacro);

                String condensedTarget = condensedTarget(asciidocMacro, option);
                String[] arr;
                int lastIndex = condensedTarget.lastIndexOf('.');
                if (lastIndex != -1 && lastIndex != condensedTarget.length() - 1) {
                    String prefix = condensedTarget.substring(0, lastIndex);
                    String extension = condensedTarget.substring(lastIndex + 1);
                    arr = new String[]{prefix, extension};
                } else {
                    arr = new String[]{condensedTarget};
                }
                String language = getLanguage(option);
                String extension = getExtension(option);

                if (arr.length == 2) {
                    language = arr[arr.length - 1];
                    language = resolveAsciidoctorLanguage(language);
                } else {
                    condensedTarget = condensedTarget + "." + extension;
                }

                String target = sourceInclude(slug, appName, condensedTarget, getClasspath(), option, language, getGuidesConfiguration().getPackageName());
                String title = Path.of(target).normalize().toString().replace("{sourceDir}/" + slug + "/", "").replace(getSourceDir(slug, option) + "/", "");

                IncludeDirective.Builder includeDirectiveBuilder = IncludeDirective.builder().attributes(asciidocMacro.attributes())
                        .target(target);
                if (getFileType() == FileType.CODE) {
                    Range range = new Range(getLicenseLoader().getNumberOfLines(), -1);
                    if (range.isValid() && asciidocMacro.attributes().stream().noneMatch(attribute -> attribute.key().equals(ATTRIBUTE_LINES))) {
                        includeDirectiveBuilder.lines(range);
                    }
                }
                String replacement = SourceBlock.builder()
                        .title(title)
                        .language(language)
                        .includeDirective(includeDirectiveBuilder.build())
                        .build()
                        .toString();
                str = str.replace(line, replacement);
            }
        }
        return str;
    }

    /**
     * Retrieves the language from the given GuidesOption.
     *
     * @param option The GuidesOption from which to retrieve the language.
     * @return The language as a String.
     */
    protected String getLanguage(GuidesOption option) {
        return option.getLanguage().toString();
    }

    /**
     * Retrieves the file extension from the given GuidesOption.
     *
     * @param option The GuidesOption from which to retrieve the file extension.
     * @return The file extension as a String.
     */
    protected String getExtension(GuidesOption option) {
        return option.getLanguage().getExtension();
    }

    /**
     * Constructs the source title based on the provided parameters.
     *
     * @param appName         The name of the application.
     * @param condensedTarget The condensed target path.
     * @param classpath       The classpath for the source.
     * @param language        The language of the source.
     * @param packageName     The package name of the source.
     * @return The constructed source title as a String.
     */
    protected String sourceTitle(
            String appName,
            String condensedTarget,
            Classpath classpath,
            String language,
            String packageName) {
        return (appName.equals(MacroSubstitution.APP_NAME_DEFAULT) ? "" : (appName + "/")) + sourceConventionFolder(classpath, language) + "/"
                + (getFileType() == FileType.CODE ? (packageName.replace(".", "/") + "/") : "")
                + condensedTarget;
    }

    /**
     * Constructs the source include path based on the provided parameters.
     *
     * @param slug            The slug of the guide.
     * @param appName         The name of the application.
     * @param condensedTarget The condensed target path.
     * @param classpath       The classpath for the source.
     * @param option          The GuidesOption containing additional options.
     * @param language        The language of the source.
     * @param packageName     The package name of the source.
     * @return The constructed source include path as a String.
     */
    protected String sourceInclude(
            String slug,
            String appName,
            String condensedTarget,
            Classpath classpath,
            GuidesOption option,
            String language,
            String packageName) {
        return "{sourceDir}/" + slug + "/" + getSourceDir(slug, option) + "/" +
                sourceTitle(appName, condensedTarget, classpath, language, packageName);
    }

    private String sourceConventionFolder(Classpath classpath, String language) {
        if (getFileType() == FileType.CODE) {
            return "src/" + classpath + "/" + language;
        } else if (getFileType() == FileType.RESOURCE) {
            return "src/" + classpath + "/resources";
        }
        throw new UnsupportedOperationException("Unimplemented sourceConventionFolder for " + getFileType());
    }

    /**
     * Retrieves the condensed target from the given AsciidocMacro and GuidesOption.
     *
     * @param asciidocMacro The AsciidocMacro from which to retrieve the target.
     * @param option        The GuidesOption containing additional options.
     * @return The condensed target as a String.
     */
    protected String condensedTarget(@NonNull AsciidocMacro asciidocMacro, GuidesOption option) {
        return asciidocMacro.target();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

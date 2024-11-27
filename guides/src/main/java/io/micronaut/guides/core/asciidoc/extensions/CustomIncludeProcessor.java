package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.*;
import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Singleton
public class CustomIncludeProcessor extends IncludeProcessor {
    private final GuideContextProvider guideContextProvider;
    private final GuidesConfiguration guidesConfiguration;

    @Override
    public boolean handles(String s) {
        return s.contains("source:");
    }

    CustomIncludeProcessor(GuideContextProvider guideContextProvider, GuidesConfiguration guidesConfiguration) {
        this.guideContextProvider = guideContextProvider;
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public void process(Document document, PreprocessorReader reader, String target, Map<String, Object> attributes) {
        target = target.replace("source:", "");
        String appName;
        if (attributes.containsKey("app")) {
            appName = attributes.get("app").toString();
        } else {
            appName = "default";
        }
        Path path = Path.of(guideContextProvider.getBaseDir(),
                guideContextProvider.getGuide().getSlug(),
                getSourceDir(guideContextProvider.getGuide().getSlug(), guideContextProvider.getOption()),
                sourceTitle(appName, target, Classpath.MAIN, guideContextProvider.getOption().getLanguage().toString(), guidesConfiguration.getPackageName()) + "." + guideContextProvider.getOption().getLanguage().getExtension());
        File file = path.toFile();
        try {
            String content = Files.readString(file.toPath());
            if (attributes.containsKey("lines")) {
                String[] lines = attributes.get("lines").toString().split("\\.\\.");
                int beginLine = Integer.parseInt(lines[0]);
                int endLine = Integer.parseInt(lines[1]);
                String[] linesContent = content.split("\n");
                if (endLine == -1) {
                    endLine = linesContent.length;
                }
                // select the lines from the content
                StringBuilder selectedLines = new StringBuilder();
                for (int i = beginLine - 1; i < endLine; i++) {
                    selectedLines.append(linesContent[i]).append("\n");
                }
                content = selectedLines.toString();
            } else if (attributes.containsKey("tags")) {
                String[] tags = attributes.get("tags").toString().split(",");
                StringBuilder selectedLines = new StringBuilder();
                for (String tag : tags) {
                    boolean tagOpened = false;
                    for (String line : content.split("\n")) {
                        if (tagOpened) {
                            selectedLines.append(line).append("\n");
                        }
                        if (line.contains("tag::" + tag)) {
                            tagOpened = true;
                        }
                        if (line.contains("end::" + tag)) {
                            tagOpened = false;
                        }
                    }
                }
                content = selectedLines.toString();
            }

            reader.pushInclude(
                    content,
                    target,
                    target,
                    1,
                    attributes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String sourceTitle(
            String appName,
            String condensedTarget,
            Classpath classpath,
            String language,
            String packageName) {
        return (appName.equals(MacroSubstitution.APP_NAME_DEFAULT) ? "" : (appName + "/")) + sourceConventionFolder(classpath, language) + "/"
                + (getFileType() == FileType.CODE ? (packageName.replace(".", "/") + "/") : "")
                + condensedTarget;
    }

    private static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    private static String sourceConventionFolder(Classpath classpath, String language) {
        if (getFileType() == FileType.CODE) {
            return "src/" + classpath + "/" + language;
        } else if (getFileType() == FileType.RESOURCE) {
            return "src/" + classpath + "/resources";
        }
        throw new UnsupportedOperationException("Unimplemented sourceConventionFolder for " + getFileType());
    }

    private static FileType getFileType() {
        return FileType.CODE;
    }
}

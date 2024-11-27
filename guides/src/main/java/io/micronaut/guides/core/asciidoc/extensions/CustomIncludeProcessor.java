package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.guides.core.GuideContextProvider;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Singleton
public class CustomIncludeProcessor extends IncludeProcessor {
    private final GuideContextProvider guideContextProvider;

    @Override
    public boolean handles(String s) {
        return s.contains("source:");
    }

    CustomIncludeProcessor(GuideContextProvider guideContextProvider) {
        this.guideContextProvider = guideContextProvider;
    }

    @Override
    public void process(Document document, PreprocessorReader reader, String target, Map<String, Object> attributes) {
        target = target.replace("source:", "");
        File file = new File(guideContextProvider.getBaseDir(), target + ".java");
        try {
            reader.pushInclude(
                    "<pre><code class=\"language-java hljs\"  data-highlighted=\"yes\">" + Files.readString(file.toPath()) + "</code></pre>",
                    target,
                    file.getAbsolutePath(),
                    1,
                    attributes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //        reader.pushInclude(
        //                sb.toString(),
        //                target,
        //                new File(".").getAbsolutePath(),
        //                1,
        //                attributes);
    }
}

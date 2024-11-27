package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.guides.core.GuideContextProvider;
import io.micronaut.guides.core.MacroSubstitution;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;
import org.asciidoctor.extension.Reader;

import java.util.List;

@Singleton
public class CustomPreprocessor extends Preprocessor {

    private final MacroSubstitution macroSubstitution;
    private final GuideContextProvider guideContextProvider;

    CustomPreprocessor(MacroSubstitution macroSubstitution, GuideContextProvider guideContextProvider) {
        this.macroSubstitution = macroSubstitution;
        this.guideContextProvider = guideContextProvider;
    }

    @Override
    public Reader process(Document document, PreprocessorReader reader) {
        List<String> lines = reader.readLines();
        String content = String.join("\n", lines);

        String result = macroSubstitution.substitute(content, guideContextProvider.getGuide(), guideContextProvider.getOption());

        return newReader(result.lines().toList());
    }
}

package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.guides.core.GuideContextProvider;
import io.micronaut.guides.core.GuidesConfiguration;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.asciidoctor.extension.Name;

import java.util.Map;

@Singleton
@Name("source")
public class SourceMacroProcessor extends BlockMacroProcessor {

    private final GuideContextProvider guideContextProvider;
    private final GuidesConfiguration guidesConfiguration;

    SourceMacroProcessor(GuideContextProvider guideContextProvider, GuidesConfiguration guidesConfiguration) {
        this.guideContextProvider = guideContextProvider;
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public StructuralNode process(StructuralNode parent, String target, Map<String, Object> attributes) {
        return null;
    }
}

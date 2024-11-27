package io.micronaut.guides.core.asciidoc.extensions;

import io.micronaut.guides.core.GuideContextProvider;
import io.micronaut.guides.core.GuidesConfiguration;
import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import jakarta.inject.Singleton;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.asciidoctor.extension.Name;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Name("source")
public class SourceMacroProcessor extends BlockMacroProcessor {

    private final GuideContextProvider guideContextProvider;
    private final GuidesConfiguration guidesConfiguration;
    private final AsciidocConfiguration asciidocConfiguration;

    SourceMacroProcessor(GuideContextProvider guideContextProvider, GuidesConfiguration guidesConfiguration, AsciidocConfiguration asciidocConfiguration) {
        this.guideContextProvider = guideContextProvider;
        this.guidesConfiguration = guidesConfiguration;
        this.asciidocConfiguration = asciidocConfiguration;

    }

    @Override
    public StructuralNode process(StructuralNode parent, String target, Map<String, Object> attributes) {
        File file = new File(guideContextProvider.getBaseDir(), target + ".java");
        Map<Object, Object> options = new HashMap<>();
        Map<String, Object> attr = new HashMap<>();
        attr.put("language", "java");
        try {
            Block block = createBlock(parent, "listing", Files.readString(file.toPath()), attr, options);
            block.setStyle("source");
            return block;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

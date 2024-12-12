package io.micronaut.guides.core.asciidoc.extensions;

import jakarta.inject.Singleton;
import org.asciidoctor.extension.*;

import java.util.List;

@Singleton
public class ExtensionsRegistry {

    private final List<IncludeProcessor> includeProcessors;
    private final List<BlockProcessor> blockProcessors;
    private final List<BlockMacroProcessor> blockMacroProcessors;
    private final List<InlineMacroProcessor> lineMacroProcessors;
    private final List<Preprocessor> preProcessors;

    ExtensionsRegistry(List<IncludeProcessor> includeProcessors, List<BlockProcessor> blockProcessors, List<BlockMacroProcessor> blockMacroProcessors, List<InlineMacroProcessor> lineMacroProcessors, List<Preprocessor> preProcessors) {
        this.includeProcessors = includeProcessors;
        this.blockProcessors = blockProcessors;
        this.blockMacroProcessors = blockMacroProcessors;
        this.lineMacroProcessors = lineMacroProcessors;
        this.preProcessors = preProcessors;
    }

    public List<IncludeProcessor> getIncludeProcessors() {
        return includeProcessors;
    }

    public List<BlockProcessor> getBlockProcessors() {
        return blockProcessors;
    }

    public List<BlockMacroProcessor> getBlockMacroProcessors() {
        return blockMacroProcessors;
    }

    public List<InlineMacroProcessor> getLineMacroProcessors() {
        return lineMacroProcessors;
    }

    public List<Preprocessor> getPreProcessors() {
        return preProcessors;
    }
}

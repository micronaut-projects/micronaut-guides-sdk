package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.GuideRenderProvider;
import jakarta.inject.Singleton;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Singleton
public class DefaultAsciidocLogger implements AsciidocLogger {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AsciidocLogger.class);

    private final GuideRenderProvider guideRenderProvider;

    DefaultAsciidocLogger(GuideRenderProvider guideRenderProvider) {
        this.guideRenderProvider = guideRenderProvider;
    }

    @Override
    public void log(LogRecord logRecord) {
        LOG.atLevel(mapSeverity(logRecord.getSeverity()))
                .log("[{}] {}: {}",
                        guideRenderProvider.getGuideRender().guide().getSlug(),
                        logRecord.getCursor() != null ? logRecord.getCursor().getFile() + " line " + logRecord.getCursor().getLineNumber() : "",
                        logRecord.getMessage());
    }

    private static Level mapSeverity(Severity severity) {
        return switch (severity) {
            case DEBUG -> Level.DEBUG;
            case WARN -> Level.WARN;
            case ERROR, FATAL -> Level.ERROR;
            default -> Level.INFO;
        };
    }
}

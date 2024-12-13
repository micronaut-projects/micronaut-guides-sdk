package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.GuideRender;
import org.asciidoctor.log.LogRecord;

public interface AsciidocLogger {
    void log(LogRecord logRecord, GuideRender guideRender);
}

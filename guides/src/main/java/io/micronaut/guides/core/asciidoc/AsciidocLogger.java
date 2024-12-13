package io.micronaut.guides.core.asciidoc;

import org.asciidoctor.log.LogHandler;
import org.asciidoctor.log.LogRecord;

public interface AsciidocLogger extends LogHandler {
    void log(LogRecord logRecord);
}

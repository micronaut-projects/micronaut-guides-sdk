package io.micronaut.guides.core.html;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.Guide;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IndexGenerator {

    @NonNull
    String renderIndex(@NonNull @NotNull List<Guide> guides);
}
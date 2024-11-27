package io.micronaut.guides.cli;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.core.App;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

@Serdeable
@JsonSchema
public class GdkApp extends App {
    @JsonPropertyDescription("The GDK Starter services' name that the app requires")
    @Nullable
    private List<String> services = new ArrayList<>();

    public @Nullable List<String> getServices() {
        return services;
    }

    public void setServices(@Nullable List<String> services) {
        this.services = services;
    }
}

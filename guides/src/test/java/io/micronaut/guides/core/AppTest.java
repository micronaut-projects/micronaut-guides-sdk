package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AppTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Test
    void typeAppPackageNameCanBeNull() {
        String name = "name";
        String packageName = null;
        ApplicationType applicationType = ApplicationType.DEFAULT;
        String framework = "Micronaut";
        List<String> emptyList = new ArrayList<>();
        boolean validateLicense = true;

        App app = new App();
        app.setName(name);
        app.setPackageName(packageName);
        app.setApplicationType(applicationType);
        app.setFramework(framework);
        Set<ConstraintViolation<App>> violations = validator.validate(app);
        assertTrue(violations.isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(App.class));
    }

    @Test
    void typeAppIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(App.class)));
    }

    @Test
    void typeAppIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(App.class)));
    }
}

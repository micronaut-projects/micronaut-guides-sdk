plugins {
    id("io.micronaut.build.internal.guides-module")
}

dependencies {
    api(libs.micronaut.starter.api)
    api(libs.managed.asciidoctorj)
    api(libs.managed.apache.compress)
    implementation(mnRss.micronaut.rss)
    implementation(mnRss.micronaut.jsonfeed.core)
    annotationProcessor(mnValidation.micronaut.validation.processor)
    implementation(mnValidation.micronaut.validation)
    annotationProcessor(mnSerde.micronaut.serde.processor)
    implementation(mnSerde.micronaut.serde.jackson)
    annotationProcessor(mnJsonSchema.micronaut.json.schema.processor)
    compileOnly(mnJsonSchema.micronaut.json.schema.annotations)
    api(mnJsonSchema.json.schema.validator)

    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testImplementation(mnTest.junit.jupiter.api)
    testRuntimeOnly(mnTest.junit.jupiter.engine)

    testImplementation(libs.jsonassert)
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Amicronaut.jsonschema.baseUri=https://guides.micronaut.io/schemas")
}
micronautBuild {
    binaryCompatibility {
        enabled = false
    }
}


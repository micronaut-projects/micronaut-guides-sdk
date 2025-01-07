plugins {
    id("io.micronaut.build.internal.guides-module")
}

repositories {
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
    mavenCentral {
        mavenContent {
            releasesOnly()
        }
    }
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
    api(mnJsonSchema.json.schema.validator)

    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testImplementation(mnTest.junit.jupiter.api)
    testRuntimeOnly(mnTest.junit.jupiter.engine)

    testImplementation(libs.jsonassert)
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
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


import io.micronaut.build.TestFramework

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
    testImplementation(libs.jsonassert)
}
micronautBuild {
    testFramework = TestFramework.JUNIT5
}
java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Amicronaut.jsonschema.baseUri=https://guides.micronaut.io/schemas")
}


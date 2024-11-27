plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.4.4"
}

version = "0.1"
group = "io.micronaut.guides.cli"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor(mnJsonSchema.micronaut.json.schema.processor)
    implementation("info.picocli:picocli")
    implementation(projects.guides)
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    compileOnly(mnJsonSchema.micronaut.json.schema.annotations)
    runtimeOnly("ch.qos.logback:logback-classic")
}


application {
    mainClass = "io.micronaut.guides.cli.GuidescliCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}



micronaut {
    version.set(libs.versions.micronaut.platform.get())
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.guides.cli.*")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}



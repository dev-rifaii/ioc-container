plugins {
    id("java-library")
}

group = "dev.rits"
version = "0.1"

repositories {
    mavenCentral()
}


configurations {
    create("benchmarkImplementation") {
        extendsFrom(configurations["implementation"])
    }

    create("benchmarkAnnotationProcessor") {
        extendsFrom(configurations["annotationProcessor"])
    }
}
sourceSets {
    create("benchmark") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

val benchmarkImplementation: Configuration by configurations.getting
val benchmarkAnnotationProcessor: Configuration by configurations.getting
dependencies {
    api("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    benchmarkImplementation("org.openjdk.jmh:jmh-core:1.37")
    benchmarkAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runBenchmark") {
    group = "Benchmark"
    description = "Benchmarks container initialization time"

    mainClass.set("org.rifaii.Benchmarker")

    classpath = sourceSets["benchmark"].runtimeClasspath
}
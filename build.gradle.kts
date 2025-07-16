import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.0"
    application
    alias(libs.plugins.vaadin)
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    // Vaadin 24 requires JDK 17+
    compilerOptions.jvmTarget = JvmTarget.JVM_17
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the stacktraces of failed tests in the CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    // Vaadin-related dependencies
    implementation(libs.vaadin.core) {
        if (vaadin.effective.productionMode.get()) {
            exclude(module = "vaadin-dev")
        }
    }
    implementation(libs.vok.db)
    implementation(libs.vaadin.security.simple)
    implementation(libs.vaadin.boot)

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation(libs.slf4j.simple)

    // db
    implementation(libs.hikaricp)
    implementation(libs.flyway)
    implementation(libs.h2)

    // test support
    testImplementation(libs.karibu.testing)
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    // Vaadin 24 requires JDK 17+
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass = "com.vaadin.securitydemo.MainKt"
}

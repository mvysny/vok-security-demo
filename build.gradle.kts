import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.14.1"
val vaadin_version = "23.3.4"

plugins {
    kotlin("jvm") version "1.8.0"
    id("application")
    id("com.vaadin") version "23.3.4"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    // Vaadin 23 requires JDK 11+ anyway
    kotlinOptions.jvmTarget = "11"
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
    implementation("com.vaadin:vaadin-core:$vaadin_version")
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vaadinonkotlin_version")
    implementation("eu.vaadinonkotlin:vok-security:$vaadinonkotlin_version")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:10.1")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.4")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.8.2")
    implementation("com.h2database:h2:2.1.214")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:1.3.23")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
}

java {
    // Vaadin 23 requires JDK 11+
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.vaadin.securitydemo.MainKt")
}

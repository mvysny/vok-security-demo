import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.14.1"
val vaadin_version = "24.0.0"

plugins {
    kotlin("jvm") version "1.8.0"
    id("application")
    id("com.vaadin") version "24.0.0"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    // Vaadin 24 requires JDK 17+
    kotlinOptions.jvmTarget = "17"
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
    implementation("com.vaadin:vaadin-core:$vaadin_version") {
        afterEvaluate {
            if (vaadin.productionMode) {
                exclude(module = "vaadin-dev-server")
            }
        }
    }
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vaadinonkotlin_version")
    implementation("com.github.mvysny.vaadin-simple-security:vaadin-simple-security:0.2")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:11.0")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.4")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.8.2")
    implementation("com.h2database:h2:2.1.214")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:2.0.2")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
}

java {
    // Vaadin 24 requires JDK 17+
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("com.vaadin.securitydemo.MainKt")
}

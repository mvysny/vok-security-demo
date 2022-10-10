import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "0.13.0"
val vaadin_version = "23.2.1"

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.gretty") version "3.0.6"
    war
    id("com.vaadin") version "23.2.1"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

val staging by configurations.creating

tasks.withType<KotlinCompile> {
    // Vaadin 23 requires JDK 11+ anyway
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    // Vaadin-on-Kotlin dependency
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vaadinonkotlin_version")
    implementation("eu.vaadinonkotlin:vok-security:$vaadinonkotlin_version")
    // Vaadin 14
    implementation("com.vaadin:vaadin-core:$vaadin_version")
    providedCompile("javax.servlet:javax.servlet-api:4.0.1")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.0")
    implementation("org.slf4j:slf4j-api:2.0.0")

    implementation(kotlin("stdlib-jdk8"))

    // db
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:8.5.12")
    implementation("com.h2database:h2:2.1.214")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v23:1.3.21")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")

    // heroku app runner
    staging("com.heroku:webapp-runner-main:9.0.52.1")
}

java {
    // Vaadin 23 requires JDK 11+
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    val stage by registering {
        dependsOn("build", copyToLib)
    }
}

vaadin {
    if (gradle.startParameter.taskNames.contains("stage")) {
        productionMode = true
    }
}

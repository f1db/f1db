plugins {
    kotlin("jvm") version "2.1.20"
}

group = "com.f1db"

val joyVersion by extra("2.1.0")
val jooqVersion by extra("3.20.1")
val junitVersion by extra("5.12.1")
val justifyVersion by extra("3.1.0")
val kotestVersion by extra("5.9.1")
val mysqlConnectorJavaVersion by extra("8.0.33")
val postgresqlVersion by extra("42.7.5")
val testcontainersVersion by extra("1.20.6")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjvm-default=all")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
    implementation("com.f1db:build-logic:1.0")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("mysql:mysql-connector-java:$mysqlConnectorJavaVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.leadpony.joy:joy-classic:$joyVersion")
    testImplementation("org.leadpony.justify:justify:$justifyVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:mysql:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation("org.postgresql:postgresql:$postgresqlVersion")
}

tasks.withType<Test>() {
    outputs.upToDateWhen { false }
    useJUnitPlatform()
    testLogging {
        events("failed", "passed", "skipped")
    }
}

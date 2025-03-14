import java.io.FileFilter

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.kotlin.kapt") version "2.1.10"
    id("org.jooq.jooq-codegen-gradle") version "3.20.1"
    id("org.jsonschema2pojo") version "1.2.2"
}

gradlePlugin {
    plugins {
        create("f1db") {
            id = "com.f1db"
            implementationClass = "com.f1db.plugin.F1DBPlugin"
        }
    }
}

val jacksonVersion by extra("2.18.3")
val joyVersion by extra("2.1.0")
val jooqVersion by extra("3.20.1")
val junitVersion by extra("5.12.0")
val justifyVersion by extra("3.1.0")
val kotestVersion by extra("5.9.1")
val mapstructVersion by extra("1.6.3")
val mysqlConnectorJavaVersion by extra("8.0.33")
val postgresqlVersion by extra("42.7.5")
val slf4jVersion by extra("2.0.16")
val sqliteJdbcVersion by extra("3.49.1.0")
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
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-smile:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.jooq:jooq:${jooqVersion}")
    implementation("org.leadpony.joy:joy-classic:$joyVersion")
    implementation("org.leadpony.justify:justify:$justifyVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
    jooqCodegen("org.jooq:jooq-meta:${jooqVersion}")
    jooqCodegen("org.jooq:jooq-meta-extensions:${jooqVersion}")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
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

sourceSets {
    val main by getting {
        java {
            srcDir("build/generated-sources/jooq")
            srcDir("build/generated-sources/jsonschema2pojo")
        }
    }
}

jooq {
    configuration {
        generator {
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources/sql/create_schema.sql"
                    }
                    property {
                        key = "defaultNameCase"
                        value = "as-is"
                    }
                }
            }
            target {
                directory = "build/generated-sources/jooq"
                packageName = "com.f1db.plugin.writer.sql"
            }
        }
    }
}

jsonSchema2Pojo {
    sourceFiles = files("$projectDir/../src/schema/current/")
    fileFilter = FileFilter { file -> file.isDirectory || file.name.endsWith(".json") }
    targetDirectory = file("build/generated-sources/jsonschema2pojo")
    removeOldOutput = true
    targetPackage = "com.f1db.plugin.schema"
    targetVersion = "21"
    dateTimeType = "java.time.OffsetDateTime"
    dateType = "java.time.LocalDate"
    timeType = "java.time.OffsetTime"
    includeAdditionalProperties = false
    isIncludeGeneratedAnnotation = false
    setInclusionLevel("ALWAYS")
    initializeCollections = false
    serializable = true
    useBigDecimals = true
    useTitleAsClassname = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
    dependsOn(tasks.named("jooqCodegen"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

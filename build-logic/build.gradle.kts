import java.io.FileFilter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.3.21"
    kotlin("kapt") version "2.3.21"
    id("org.jooq.jooq-codegen-gradle") version "3.21.9"
    id("org.jsonschema2pojo") version "1.3.3"
}

group = "com.f1db"
version = "1.0"

gradlePlugin {
    plugins {
        create("f1db") {
            id = "com.f1db"
            implementationClass = "com.f1db.plugin.F1DBPlugin"
        }
    }
}

val jacksonVersion = "2.22.0"
val jooqVersion = "3.21.6"
val mapstructVersion = "1.6.3"
val slf4jVersion = "2.0.18"
val sqliteJdbcVersion = "3.53.2.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-smile:$jacksonVersion")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api("org.jooq:jooq:${jooqVersion}")
    api("org.mapstruct:mapstruct:$mapstructVersion")
    api("org.slf4j:slf4j-simple:$slf4jVersion")
    api("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
    jooqCodegen("org.jooq:jooq-meta:${jooqVersion}")
    jooqCodegen("org.jooq:jooq-meta-extensions:${jooqVersion}")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
}

sourceSets {
    getByName("main") {
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
    targetVersion = "25"
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

tasks.withType<KotlinCompile> {
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
    dependsOn(tasks.named("jooqCodegen"))
}

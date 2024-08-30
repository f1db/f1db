plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.kapt") version "1.9.25"
    id("org.jooq.jooq-codegen-gradle") version "3.19.11"
    id("org.jsonschema2pojo") version "1.2.1"
}

gradlePlugin {
    plugins {
        create("f1db") {
            id = "com.f1db"
            implementationClass = "com.f1db.plugin.F1DBPlugin"
        }
    }
}

val jacksonVersion by extra("2.17.2")
val joyVersion by extra("2.1.0")
val jooqVersion by extra("3.19.11")
val justifyVersion by extra("3.1.0")
val mapstructVersion by extra("1.6.0")
val slf4jVersion by extra("1.7.36")
val sqliteJdbcVersion by extra("3.46.1.0")

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
    targetDirectory = file("build/generated-sources/jsonschema2pojo")
    removeOldOutput = true
    targetPackage = "com.f1db.plugin.schema"
    targetVersion = "17"
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
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
    dependsOn(tasks.named("jooqCodegen"))
}

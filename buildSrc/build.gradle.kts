plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
    id("jsonschema2pojo")
}

gradlePlugin {
    plugins {
        create("onlyf1db") {
            id = "com.onlyf1.db"
            implementationClass = "com.onlyf1.db.plugin.OnlyF1DBPlugin"
        }
    }
}

val jacksonVersion by extra("2.14.1")
val jdbiVersion by extra("3.36.0")
val joyVersion by extra("2.1.0")
val justifyVersion by extra("3.1.0")
val mapstructVersion by extra("1.5.3.Final")
val slf4jVersion by extra("1.7.36")
val springVersion by extra("6.0.4")
val sqliteJdbcVersion by extra("3.40.0.0")

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
    implementation("org.jdbi:jdbi3-core:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbiVersion")
    implementation("org.leadpony.joy:joy-classic:$joyVersion")
    implementation("org.leadpony.justify:justify:$justifyVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
}

configure<org.jsonschema2pojo.gradle.JsonSchemaExtension> {
    sourceFiles = files("$projectDir/../src/schema/current/")
    removeOldOutput = true
    targetPackage = "com.onlyf1.db.schema"
    targetVersion = "17"
    dateType = "java.time.LocalDate"
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
    mustRunAfter(tasks.named("generateJsonSchema2Pojo"))
}

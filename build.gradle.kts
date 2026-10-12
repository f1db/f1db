plugins {
    distribution
    kotlin("jvm") version "2.3.21"
    id("com.f1db")
    id("org.jreleaser") version "1.26.0"
}

group = "com.f1db"

val currentSeasonProperty = project.property("currentSeason") as String
val currentSeasonFinishedProperty = project.property("currentSeasonFinished") as String

val joyVersion = "2.1.0"
val junitVersion = "6.1.3"
val justifyVersion = "3.1.0"
val kotestVersion = "6.2.5"
val mysqlConnectorJavaVersion = "8.0.33"
val postgresqlVersion = "42.7.14"
val testcontainersVersion = "1.21.4"

f1db {
    sourceDir.set(layout.projectDirectory.dir("src/data"))
    outputDir.set(layout.buildDirectory.dir("data"))
    currentSeason.set(currentSeasonProperty.toInt())
    currentSeasonFinished.set(currentSeasonFinishedProperty.toBoolean())
}

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

distributions {
    create("csv") {
        distributionBaseName.set("f1db-csv")
        contents {
            from(layout.buildDirectory.dir("data/csv")) {
                into("package/data")
                include("*.csv")
            }
            into("/")
        }
    }
    create("json-single") {
        distributionBaseName.set("f1db-json-single")
        contents {
            from(layout.buildDirectory.file("data/json/f1db.json")) {
                into("package/data")
            }
            from(layout.projectDirectory.file("src/schema/current/single/f1db.schema.json")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("json-splitted") {
        distributionBaseName.set("f1db-json-splitted")
        contents {
            from(layout.buildDirectory.dir("data/json")) {
                into("package/data")
                include("*.json")
                exclude("f1db.json")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                into("package/data")
                include("*.schema.json")
            }
            into("/")
        }
    }
    create("sql-mysql") {
        distributionBaseName.set("f1db-sql-mysql")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-mysql.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sql-mysql-single-inserts") {
        distributionBaseName.set("f1db-sql-mysql-single-inserts")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-mysql-single-inserts.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sql-postgresql") {
        distributionBaseName.set("f1db-sql-postgresql")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-postgresql.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sql-postgresql-single-inserts") {
        distributionBaseName.set("f1db-sql-postgresql-single-inserts")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-postgresql-single-inserts.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sql-sqlite") {
        distributionBaseName.set("f1db-sql-sqlite")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-sqlite.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sql-sqlite-single-inserts") {
        distributionBaseName.set("f1db-sql-sqlite-single-inserts")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-sqlite-single-inserts.sql")) {
                into("package/data")
            }
            into("/")
        }
    }
    create("sqlite") {
        distributionBaseName.set("f1db-sqlite")
        contents {
            from(layout.buildDirectory.file("data/sqlite/f1db.db")) {
                into("package/data")
            }
            into("/")
        }
    }
    // TODO: old zip distributions; remove later.
    create("csv-zip") {
        distributionBaseName.set("f1db-csv-zip")
        contents {
            from(layout.buildDirectory.dir("data/csv")) {
                include("*.csv")
            }
            into("/")
        }
    }
    create("json-single-zip") {
        distributionBaseName.set("f1db-json-single-zip")
        contents {
            from(layout.buildDirectory.file("data/json/f1db.json"))
            from(layout.projectDirectory.file("src/schema/current/single/f1db.schema.json"))
            into("/")
        }
    }
    create("json-splitted-zip") {
        distributionBaseName.set("f1db-json-splitted-zip")
        contents {
            from(layout.buildDirectory.dir("data/json")) {
                include("*.json")
                exclude("f1db.json")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
            into("/")
        }
    }
    create("smile-single-zip") {
        distributionBaseName.set("f1db-smile-single-zip")
        contents {
            from(layout.buildDirectory.file("data/smile/f1db.sml"))
            from(layout.projectDirectory.file("src/schema/current/single/f1db.schema.json"))
            into("/")
        }
    }
    create("smile-splitted-zip") {
        distributionBaseName.set("f1db-smile-splitted-zip")
        contents {
            from(layout.buildDirectory.dir("data/smile")) {
                include("*.sml")
                exclude("f1db.sml")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
            into("/")
        }
    }
    create("sql-mysql-zip") {
        distributionBaseName.set("f1db-sql-mysql-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-mysql.sql"))
            into("/")
        }
    }
    create("sql-mysql-single-inserts-zip") {
        distributionBaseName.set("f1db-sql-mysql-single-inserts-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-mysql-single-inserts.sql"))
            into("/")
        }
    }
    create("sql-postgresql-zip") {
        distributionBaseName.set("f1db-sql-postgresql-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-postgresql.sql"))
            into("/")
        }
    }
    create("sql-postgresql-single-inserts-zip") {
        distributionBaseName.set("f1db-sql-postgresql-single-inserts-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-postgresql-single-inserts.sql"))
            into("/")
        }
    }
    create("sql-sqlite-zip") {
        distributionBaseName.set("f1db-sql-sqlite-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-sqlite.sql"))
            into("/")
        }
    }
    create("sql-sqlite-single-inserts-zip") {
        distributionBaseName.set("f1db-sql-sqlite-single-inserts-zip")
        contents {
            from(layout.buildDirectory.file("data/sql/f1db-sql-sqlite-single-inserts.sql"))
            into("/")
        }
    }
    create("sqlite-zip") {
        distributionBaseName.set("f1db-sqlite-zip")
        contents {
            from(layout.buildDirectory.file("data/sqlite/f1db.db"))
            into("/")
        }
    }
}

jreleaser {
    project {
        name.set("f1db")
        versionPattern.set("CALVER:YYYY.MINOR.MICRO[.MODIFIER]")
        description.set("F1DB Open Source Formula 1 Database")
        license.set("CC-BY-4.0")
        copyright.set("F1DB")
    }
    files {
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-csv-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-csv.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-json-single-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-json-single.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-json-splitted-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-json-splitted.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-mysql-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-mysql.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-mysql-single-inserts-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-mysql-single-inserts.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-postgresql-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-postgresql.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-postgresql-single-inserts-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-postgresql-single-inserts.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-sqlite-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-sqlite.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-sqlite-single-inserts-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sql-sqlite-single-inserts.tgz")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sqlite-{{projectVersion}}.tgz").get().asFile.path)
            transform.set("f1db-sqlite.tgz")
        }
        // TODO: old zip release artifacts; remove later.
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-csv-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-csv.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-json-single-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-json-single.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-json-splitted-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-json-splitted.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-smile-single-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-smile-single.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-smile-splitted-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-smile-splitted.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-mysql-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-mysql.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-mysql-single-inserts-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-mysql-single-inserts.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-postgresql-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-postgresql.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-postgresql-single-inserts-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-postgresql-single-inserts.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-sqlite-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-sqlite.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sql-sqlite-single-inserts-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sql-sqlite-single-inserts.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/f1db-sqlite-zip-{{projectVersion}}.zip").get().asFile.path)
            transform.set("f1db-sqlite.zip")
        }
    }
    release {
        github {
            enabled.set(true)
            repoOwner.set("f1db")
            name.set("f1db")
            username.set("marceloverdijk")
            commitAuthor {
                name.set("F1DB")
                email.set("info@f1db.com")
            }
            prerelease {
                enabled.set(true)
                pattern.set("""^\d+\.\d+\.\d+-.+$""")
            }
            skipTag.set(true)
        }
    }
}

// TODO: old zip distributions; disable later.
// tasks.withType<Tar> {
//     enabled = false
// }

tasks.withType<Tar> {
    archiveExtension.set("tgz")
    compression = Compression.GZIP
    isPreserveFileTimestamps = true
}

tasks.withType<Test> {
    outputs.upToDateWhen { false }
    useJUnitPlatform()
    testLogging {
        events("failed", "passed", "skipped")
    }
}

tasks.named("jreleaserFullRelease") {
    dependsOn("build")
}

tasks.register<Exec>("cloc") {
    group = "other"
    description = "Counts lines of data."
    commandLine("/opt/homebrew/bin/cloc", "src/data")
}

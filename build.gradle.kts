plugins {
    distribution
    id("com.f1db")
    id("org.jreleaser") version "1.15.0"
    kotlin("jvm") version "1.9.22"
}

group = "com.f1db"

val projectName = project.name
val currentSeasonYear: String by project
val currentSeasonFinished: String by project

f1db {
    sourceDir.set(layout.projectDirectory.dir("src/data"))
    schemaDir.set(layout.projectDirectory.dir("src/schema/current"))
    outputDir.set(layout.buildDirectory.dir("data"))
    currentSeason {
        year.set(currentSeasonYear.toInt())
        finished.set(currentSeasonFinished.toBoolean())
    }
}

repositories {
    mavenCentral()
}

distributions {
    create("csv") {
        distributionBaseName.set("${projectName}-csv")
        contents {
            from(layout.buildDirectory.dir("data/csv"))
            into("/")
            include("*.csv")
        }
    }
    create("json-single") {
        distributionBaseName.set("${projectName}-json-single")
        contents {
            from(layout.buildDirectory.file("data/json/${projectName}.json"))
            from(layout.projectDirectory.file("src/schema/current/single/${projectName}.schema.json"))
            into("/")
        }
    }
    create("json-splitted") {
        distributionBaseName.set("${projectName}-json-splitted")
        contents {
            from(layout.buildDirectory.dir("data/json")) {
                include("*.json")
                exclude("${projectName}.json")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
            into("/")
        }
    }
    create("smile-single") {
        distributionBaseName.set("${projectName}-smile-single")
        contents {
            from(layout.buildDirectory.file("data/smile/${projectName}.sml"))
            from(layout.projectDirectory.file("src/schema/current/single/${projectName}.schema.json"))
            into("/")
        }
    }
    create("smile-splitted") {
        distributionBaseName.set("${projectName}-smile-splitted")
        contents {
            from(layout.buildDirectory.dir("data/smile")) {
                include("*.sml")
                exclude("${projectName}.sml")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
            into("/")
        }
    }
    create("sql-mysql") {
        distributionBaseName.set("${projectName}-sql-mysql")
        contents {
            from(layout.buildDirectory.file("data/sql/${projectName}-sql-mysql.sql"))
            into("/")
        }
    }
    create("sql-postgresql") {
        distributionBaseName.set("${projectName}-sql-postgresql")
        contents {
            from(layout.buildDirectory.file("data/sql/${projectName}-sql-postgresql.sql"))
            into("/")
        }
    }
    create("sql-sqlite") {
        distributionBaseName.set("${projectName}-sql-sqlite")
        contents {
            from(layout.buildDirectory.file("data/sql/${projectName}-sql-sqlite.sql"))
            into("/")
        }
    }
    create("sqlite") {
        distributionBaseName.set("${projectName}-sqlite")
        contents {
            from(layout.buildDirectory.file("data/sqlite/${projectName}.db"))
            into("/")
        }
    }
}

jreleaser {
    project {
        name.set(projectName)
        versionPattern.set("CALVER:YYYY.MINOR.MICRO[.MODIFIER]")
        description.set("F1DB Open Source Formula 1 Database")
        license.set("CC-BY-4.0")
        copyright.set("F1DB")
    }
    files {
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-csv-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-csv.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-json-single-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-json-single.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-json-splitted-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-json-splitted.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-smile-single-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-smile-single.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-smile-splitted-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-smile-splitted.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-sql-mysql-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-sql-mysql.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-sql-postgresql-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-sql-postgresql.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-sql-sqlite-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-sql-sqlite.zip")
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-sqlite-{{projectVersion}}.zip").get().asFile.path)
            transform.set("${projectName}-sqlite.zip")
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
            skipTag.set(true)
        }
    }
}

tasks.withType<Tar>().configureEach {
    enabled = false
}

tasks.withType<Test>().configureEach {
    enabled = true
}
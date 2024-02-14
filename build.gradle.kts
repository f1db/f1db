plugins {
    distribution
    id("com.f1db")
    id("org.jreleaser") version "1.10.0"
}

group = "com.f1db"

val projectName = project.name
val currentSeasonYear: String by project
val currentSeasonFinished: String by project
val currentSeasonDriversChampionshipDecided: String by project
val currentSeasonConstructorsChampionshipDecided: String by project

f1db {
    sourceDir.set(layout.projectDirectory.dir("src/data"))
    schemaDir.set(layout.projectDirectory.dir("src/schema/current"))
    outputDir.set(layout.buildDirectory.dir("data"))
    currentSeason {
        year.set(currentSeasonYear.toInt())
        finished.set(currentSeasonFinished.toBoolean())
        driversChampionshipDecided.set(currentSeasonDriversChampionshipDecided.toBoolean())
        constructorsChampionshipDecided.set(currentSeasonConstructorsChampionshipDecided.toBoolean())
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
            include("*.csv")
        }
    }
    create("json-single") {
        distributionBaseName.set("${projectName}-json-single")
        contents {
            from(layout.buildDirectory.file("data/json/${projectName}.json"))
            from(layout.projectDirectory.file("src/schema/current/single/${projectName}.schema.json"))
        }
    }
    create("json-splitted") {
        distributionBaseName.set("${projectName}-json-splitted")
        contents {
            from(layout.buildDirectory.dir("data/json")) {
                include("*.json")
                exclude("${projectName}-single.json")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
        }
    }
    create("smile-single") {
        distributionBaseName.set("${projectName}-smile-single")
        contents {
            from(layout.buildDirectory.file("data/smile/${projectName}.sml"))
            from(layout.projectDirectory.file("src/schema/current/single/${projectName}.schema.json"))
        }
    }
    create("smile-splitted") {
        distributionBaseName.set("${projectName}-smile-splitted")
        contents {
            from(layout.buildDirectory.dir("data/smile")) {
                include("*.sml")
                exclude("${projectName}-single.sml")
            }
            from(layout.projectDirectory.dir("src/schema/current/splitted")) {
                include("*.schema.json")
            }
        }
    }
    create("sqlite") {
        distributionBaseName.set("${projectName}-sqlite")
        contents {
            from(layout.buildDirectory.file("data/sqlite/${projectName}.db"))
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
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-json-single-{{projectVersion}}.zip").get().asFile.path)
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-json-splitted-{{projectVersion}}.zip").get().asFile.path)
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-smile-single-{{projectVersion}}.zip").get().asFile.path)
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-smile-splitted-{{projectVersion}}.zip").get().asFile.path)
        }
        artifact {
            setPath(layout.buildDirectory.file("distributions/${projectName}-sqlite-{{projectVersion}}.zip").get().asFile.path)
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

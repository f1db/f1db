plugins {
    distribution
    id("com.onlyf1.db")
    id("org.jreleaser") version "1.4.0"
}

group = "com.onlyf1"

val currentSeasonYear: String by project
val currentSeasonFinished: String by project
val currentSeasonDriversChampionshipDecided: String by project
val currentSeasonConstructorsChampionshipDecided: String by project

onlyf1db {
    sourceDir.set(layout.projectDirectory.dir("src/data"))
    outputDir.set(layout.projectDirectory.dir("build/data"))
    schemaDir.set(layout.projectDirectory.dir("src/schema/current"))
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
        distributionBaseName.set("${project.name}-csv")
        contents {
            from("$buildDir/data/csv")
            include("*.csv")
        }
    }
    create("json-single") {
        distributionBaseName.set("${project.name}-json-single")
        contents {
            from("$buildDir/data/json/${project.name}.json")
            from("$projectDir/src/schema/current/single/${project.name}.schema.json")
        }
    }
    create("json-splitted") {
        distributionBaseName.set("${project.name}-json-splitted")
        contents {
            from("$buildDir/data/json") {
                include("*.json")
                exclude("${project.name}-single.json")
            }
            from("$projectDir/src/schema/current/splitted") {
                include("*.schema.json")
            }
        }
    }
    create("smile-single") {
        distributionBaseName.set("${project.name}-smile-single")
        contents {
            from("$buildDir/data/smile/${project.name}.sml")
            from("$projectDir/src/schema/current/single/${project.name}.schema.json")
        }
    }
    create("smile-splitted") {
        distributionBaseName.set("${project.name}-smile-splitted")
        contents {
            from("$buildDir/data/smile") {
                include("*.sml")
                exclude("${project.name}-single.sml")
            }
            from("$projectDir/src/schema/current/splitted") {
                include("*.schema.json")
            }
        }
    }
    create("sqlite") {
        distributionBaseName.set("${project.name}-sqlite")
        contents {
            from("$buildDir/data/sqlite/${project.name}.db")
        }
    }
}

jreleaser {
    project {
        name.set("${project.name}")
        versionPattern.set("CALVER:YYYY.MINOR.MICRO[.MODIFIER]")
        description.set("OnlyF1 Open Source Formula 1 Database")
        license.set("CC-BY-4.0")
        copyright.set("OnlyF1")
    }
    files {
        artifact {
            setPath("$buildDir/distributions/${project.name}-csv-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${project.name}-json-single-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${project.name}-json-splitted-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${project.name}-smile-single-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${project.name}-smile-splitted-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${project.name}-sqlite-{{projectVersion}}.zip")
        }
    }
    release {
        github {
            enabled.set(true)
            repoOwner.set("onlyf1")
            name.set("onlyf1-db")
            username.set("marceloverdijk")
            commitAuthor {
                name.set("OnlyF1")
                email.set("info@onlyf1.com")
            }
            skipTag.set(true)
        }
    }
}

tasks.withType<Tar>().configureEach {
    enabled = false
}

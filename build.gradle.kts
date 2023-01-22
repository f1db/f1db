plugins {
    distribution
    id("com.onlyf1.db")
    id("org.jreleaser") version "1.4.0"
}

group = "com.onlyf1"

val projectName = project.name
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
        distributionBaseName.set("${projectName}-csv")
        contents {
            from("$buildDir/data/csv")
            include("*.csv")
        }
    }
    create("json-single") {
        distributionBaseName.set("${projectName}-json-single")
        contents {
            from("$buildDir/data/json/${projectName}.json")
            from("$projectDir/src/schema/current/single/${projectName}.schema.json")
        }
    }
    create("json-splitted") {
        distributionBaseName.set("${projectName}-json-splitted")
        contents {
            from("$buildDir/data/json") {
                include("*.json")
                exclude("${projectName}-single.json")
            }
            from("$projectDir/src/schema/current/splitted") {
                include("*.schema.json")
            }
        }
    }
    create("smile-single") {
        distributionBaseName.set("${projectName}-smile-single")
        contents {
            from("$buildDir/data/smile/${projectName}.sml")
            from("$projectDir/src/schema/current/single/${projectName}.schema.json")
        }
    }
    create("smile-splitted") {
        distributionBaseName.set("${projectName}-smile-splitted")
        contents {
            from("$buildDir/data/smile") {
                include("*.sml")
                exclude("${projectName}-single.sml")
            }
            from("$projectDir/src/schema/current/splitted") {
                include("*.schema.json")
            }
        }
    }
    create("sqlite") {
        distributionBaseName.set("${projectName}-sqlite")
        contents {
            from("$buildDir/data/sqlite/${projectName}.db")
        }
    }
}

jreleaser {
    project {
        name.set(projectName)
        versionPattern.set("CALVER:YYYY.MINOR.MICRO[.MODIFIER]")
        description.set("OnlyF1 Open Source Formula 1 Database")
        license.set("CC-BY-4.0")
        copyright.set("OnlyF1")
    }
    files {
        artifact {
            setPath("$buildDir/distributions/${projectName}-csv-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${projectName}-json-single-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${projectName}-json-splitted-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${projectName}-smile-single-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${projectName}-smile-splitted-{{projectVersion}}.zip")
        }
        artifact {
            setPath("$buildDir/distributions/${projectName}-sqlite-{{projectVersion}}.zip")
        }
    }
    release {
        github {
            enabled.set(true)
            repoOwner.set("onlyf1com")
            name.set("onlyf1-db")
            username.set("marceloverdijk")
            commitAuthor {
                name.set("OnlyF1-DB")
                email.set("info@onlyf1.com")
            }
            skipTag.set(true)
        }
    }
}

tasks.withType<Tar>().configureEach {
    enabled = false
}

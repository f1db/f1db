package com.f1db.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.DistributionContainer

/**
 * The F1DB plugin.
 *
 * @author Marcel Overdijk
 */
abstract class F1DBPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val version = project.version.toString()

        val extension = project.extensions.create("f1db", F1DBPluginExtension::class.java)

        val dist = project.tasks.register("dist", F1DBGenerateDistributionsTask::class.java) { task ->
            task.sourceDir.set(extension.sourceDir)
            task.outputDir.set(extension.outputDir)
            task.currentSeason.set(extension.currentSeason)
            task.currentSeasonFinished.set(extension.currentSeasonFinished)
        }

        val distributions = project.extensions.getByType(
            DistributionContainer::class.java
        )

        distributions.all { distribution ->
            // TODO: old zip distributions; remove later.
            if (distribution.name.endsWith("-zip")) {
                return@all
            }
            val distributionName = distribution.name
            val packageJson = project.tasks.register("${distributionName}PackageJson") { task ->
                val outputFile = project.layout.buildDirectory.file(
                    "packageJsons/$distributionName/package.json"
                )
                task.group = "Generate"
                task.description = "Generates the package.json for the $distributionName distribution."
                task.inputs.property("version", version)
                task.inputs.property("name", distributionName)
                task.outputs.file(outputFile)
                task.doLast {
                    val file = outputFile.get().asFile
                    file.parentFile.mkdirs()
                    file.writeText(
                        """
                        {
                          "name": "@f1db/$distributionName",
                          "version": "$version",
                          "files": ["data/"],
                          "exports": {
                            "./*": "./data/*"
                          }
                        }
                        """.trimIndent()
                    )
                }
            }

            distribution.contents.from(packageJson) {
                it.into("package")
            }
        }

        project.tasks.matching { it.name.endsWith("DistZip") || it.name.endsWith("DistTar") }.configureEach {
            it.dependsOn(dist)
        }
    }
}

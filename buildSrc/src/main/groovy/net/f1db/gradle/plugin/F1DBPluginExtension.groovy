package net.f1db.gradle.plugin

import java.time.LocalDate

/**
 * The F1DB plugin extension.
 *
 * @author Marcel Overdijk
 */
class F1DBPluginExtension {
    File jsonSchemaFile
    int currentSeason = LocalDate.now().year
    boolean currentSeasonFinished = false
    boolean wdcDecided = false
    boolean wccDecided = false
    boolean indentOutput = false
}

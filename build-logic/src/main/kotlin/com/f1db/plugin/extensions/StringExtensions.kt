package com.f1db.plugin.extensions

import java.time.Duration

fun String?.toMillis(): Int? {
    if (this.isNullOrBlank() || this == "SHC" || this.matches("""^\+(\d+) lap(s)?$""".toRegex())) {
        return null
    }

    var str = this
    var hours = 0L
    var minutes = 0L
    var seconds: Long
    var millis: Long

    // Get millis.
    var index = str.lastIndexOf(".")
    millis = str.substring(index + 1).toLong()
    str = str.substring(0, index)

    // Get seconds.
    index = str.lastIndexOf(":")
    if (index == -1) {
        if (str.startsWith("+")) {
            str = str.substring(1)
        }
        seconds = str.toLong()
        str = ""
    } else {
        seconds = str.substring(index + 1).toLong()
        str = str.substring(0, index)
    }

    // Get minutes.
    if (str.length > 0) {
        index = str.lastIndexOf(":")
        if (index == -1) {
            if (str.startsWith("+")) {
                str = str.substring(1)
            }
            minutes = str.toLong()
            str = ""
        } else {
            minutes = str.substring(index + 1).toLong()
            str = str.substring(0, index)
        }
    }

    // Get hours.
    if (str.length > 0) {
        hours = str.toLong()
    }

    // Return millis.
    return Duration.ofDays(0)
            .plusHours(hours)
            .plusMinutes(minutes)
            .plusSeconds(seconds)
            .plusMillis(millis)
            .toMillis()
            .toInt()
}

fun String?.toRounds(): List<Int> {
    val rounds = mutableListOf<Int>()
    val roundsTextSplit = this?.split(",")
    roundsTextSplit?.forEach {
        if (it.contains("-")) {
            val range = it.split("-")
            val start = range[0].toInt()
            val end = range[1].toInt()
            for (round in start..end) {
                rounds.add(round)
            }
        } else {
            rounds.add(it.toInt())
        }
    }
    return rounds
}

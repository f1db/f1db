package com.f1db.plugin.extensions

import com.f1db.plugin.schema.F1DbSplitted
import com.f1db.plugin.schema.single.F1db

val F1db.splitted: F1DbSplitted
    get() = F1DbSplitted(this)

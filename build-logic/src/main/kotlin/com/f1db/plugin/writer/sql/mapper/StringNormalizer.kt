package com.f1db.plugin.writer.sql.mapper

import com.ibm.icu.text.Transliterator
import org.mapstruct.Named
import java.util.Locale

object StringNormalizer {

    private val transliterator = ThreadLocal.withInitial {
        Transliterator.getInstance("Any-Latin; Latin-ASCII")
    }

    @JvmStatic
    @Named("normalize")
    fun normalize(value: String?): String? =
        value?.let {
            transliterator.get().transliterate(it).lowercase(Locale.ROOT)
        }
}

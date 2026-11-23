package com.f1db.plugin.writer.sql.mapper

import org.mapstruct.Named
import java.text.Normalizer
import java.util.Locale

object StringNormalizer {

    private val combiningMarks = Regex("\\p{M}+")

    private val replacements = mapOf(
        'æ' to "ae",
        'Æ' to "AE",
        'đ' to "d",
        'Đ' to "D",
        'ð' to "d",
        'Ð' to "D",
        'ħ' to "h",
        'Ħ' to "H",
        'ı' to "i",
        'ĸ' to "k",
        'ł' to "l",
        'Ł' to "L",
        'ŋ' to "ng",
        'Ŋ' to "NG",
        'ø' to "o",
        'Ø' to "O",
        'œ' to "oe",
        'Œ' to "OE",
        'ß' to "ss",
        'ẞ' to "SS",
        'ŧ' to "t",
        'Ŧ' to "T",
        'þ' to "th",
        'Þ' to "TH"
    )
    
    @JvmStatic
    @Named("normalize")
    fun normalize(value: String?): String? =
        value?.let {
            buildString {
                Normalizer.normalize(it, Normalizer.Form.NFD)
                    .replace(combiningMarks, "")
                    .forEach { char ->
                        append(replacements[char] ?: char)
                    }
            }.lowercase(Locale.ROOT)
        }
}

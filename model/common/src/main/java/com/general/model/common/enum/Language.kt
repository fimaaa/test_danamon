package com.general.model.common.enum

import java.util.Locale

enum class Language(val country: String, val language: String) {
    DEFAULT("US", "en"),
    INDONESIA("id", "in");

    fun toLocale(): Locale = Locale(language)
}
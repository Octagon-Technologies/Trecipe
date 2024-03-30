package com.octagontechnologies.trecipe.utils

import androidx.core.text.HtmlCompat
import java.util.*

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

fun String.lowercase() =
    lowercase(Locale.ROOT)


fun String?.fromHtml() =
    HtmlCompat.fromHtml(this ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
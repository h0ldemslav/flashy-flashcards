@file:Suppress("DEPRECATION")

package cz.mendelu.pef.flashyflashcards.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleUtils {
    fun setLocale(context: Context, language: String) {
        updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String) {
        context.resources.apply {
            val locale = Locale(language)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)

            context.resources.updateConfiguration(config, displayMetrics)
        }
    }
}
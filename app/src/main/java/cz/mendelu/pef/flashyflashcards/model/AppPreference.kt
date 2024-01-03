package cz.mendelu.pef.flashyflashcards.model

import androidx.annotation.StringRes

data class AppPreference(
    @StringRes
    var displayName: Int,
    @StringRes
    var displayValue: Int,
    var name: String,
    var value: String,
)

data class AppPreferenceValue(
    var appPreferenceName: String,
    @StringRes
    var displayName: Int,
    var name: String
)
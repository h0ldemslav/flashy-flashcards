package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import cz.mendelu.pef.flashyflashcards.model.AppPreference
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceValue

interface SettingsScreenActions {

    fun updateAppPreference(displayName: Int, displayValue: Int)
    fun getAllAppPreferenceValues(appPreference: AppPreference): List<AppPreferenceValue>
}
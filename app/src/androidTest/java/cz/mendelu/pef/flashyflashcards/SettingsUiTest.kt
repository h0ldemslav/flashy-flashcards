package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import cz.mendelu.pef.flashyflashcards.navigation.DestinationsNavHostWrapper
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import cz.mendelu.pef.flashyflashcards.ui.screens.settings.SettingsScreen
import cz.mendelu.pef.flashyflashcards.ui.screens.settings.TestTagSettingOptionRow
import cz.mendelu.pef.flashyflashcards.ui.theme.FlashyFlashcardsTheme
import cz.mendelu.pef.flashyflashcards.utils.LocaleUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsUiTest {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testThemeSettingChange() {
        with(composeTestRule) {
            val themeSettingLabel = composeTestRule.activity.getString(R.string.app_theme)
            val lightThemeLabel = composeTestRule.activity.getString(R.string.theme_light)
            val darkThemeLabel = composeTestRule.activity.getString(R.string.theme_dark)
            val theme = mutableStateOf(lightThemeLabel)

            activity.setContent {
                navController = rememberNavController()

                FlashyFlashcardsTheme(darkTheme = theme.value == darkThemeLabel) {
                    DestinationsNavHostWrapper(
                        navGraph = NavGraphs.settings,
                        navController = navController
                    )
                }
            }

            onNodeWithText(themeSettingLabel).performClick()
            waitForIdle()

            onAllNodesWithTag(TestTagSettingOptionRow)
                .onLast()
                .performClick()

            theme.value = darkThemeLabel
            
            waitForIdle()

            Thread.sleep(1000)

            onNodeWithText(darkThemeLabel).assertExists()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testLanguageSettingChange() {
        with(composeTestRule) {
            val language = mutableStateOf(AppPreferenceConstants.LANG_EN)
            val languageSettingLabel = activity.getString(R.string.app_language)
            val csLanguageLabel = activity.getString(R.string.language_cs)

            activity.setContent {
                navController = rememberNavController()

                SettingsScreen(navController = navController)

                if (language.value != AppPreferenceConstants.LANG_EN) {
                    LocaleUtils.setLocale(LocalContext.current, language.value)
                }
            }

            onNodeWithText(languageSettingLabel).performClick()
            waitForIdle()

            onNodeWithText(csLanguageLabel).performClick()
            Thread.sleep(2000)

            language.value = AppPreferenceConstants.LANG_CS
            waitForIdle()

            Thread.sleep(2000)
        }
    }
}
package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.flashyflashcards.navigation.DestinationsNavHostWrapper
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagAddCollectionButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagAddWordButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionNameTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSaveButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionsMoreActions
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagSaveWordButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagTestMode
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagTrainingMode
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagWordNameTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagWordTranslationTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards.TestTagAnswerTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards.TestTagFlashcard
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards.TestTagNextFlashcardButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards.TestTagResultTitle
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards.TestTagTestHistory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FlashcardsUiTest {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = rememberNavController()

            DestinationsNavHostWrapper(
                navGraph = NavGraphs.collections,
                navController = navController
            )
        }
    }

    @Test
    fun testCollectionTrainingMode() {
        val name = "Hokej"
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            onNodeWithText(name).performClick()
            waitForIdle()

            val word: Pair<String, String> = Pair("Puk", "Puck")

            onNodeWithTag(TestTagAddWordButton).performClick()
            waitForIdle()

            addWord(word.first, word.second)
            waitForIdle()

            onNodeWithTag(TestTagTrainingMode)
                .assertIsDisplayed()
                .performClick()

            waitForIdle()

            onNodeWithTag(TestTagAnswerTextField)
                .assertIsDisplayed()
                .performTextInput("")
            onNodeWithTag(TestTagNextFlashcardButton)
                .assertIsDisplayed()
                .performClick()

            val flashCardError = composeTestRule.activity.getString(R.string.incorrect_answer)
            onNodeWithText(flashCardError).assertIsDisplayed()

            onNodeWithTag(TestTagFlashcard)
                .assertIsDisplayed()
                .performClick()
            onNodeWithText(word.second).assertIsDisplayed()
            onNodeWithTag(TestTagAnswerTextField).performTextInput(word.second)
            onNodeWithTag(TestTagNextFlashcardButton).performClick()
            waitForIdle()

            onNodeWithTag(TestTagResultTitle).assertIsDisplayed()

            Thread.sleep(1000)
        }
    }

    @Test
    fun testCollectionTestMode() {
        val name = "Popularni sporty"
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            onNodeWithText(name).performClick()
            waitForIdle()

            val word: Pair<String, String> = Pair("Hokej", "Hockey")

            onNodeWithTag(TestTagAddWordButton).performClick()
            waitForIdle()

            addWord(word.first, word.second)
            waitForIdle()

            onNodeWithTag(TestTagCollectionsMoreActions)
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(TestTagTestMode).performClick()
            onNodeWithTag(TestTagFlashcard).performClick()

            // Hint should be turned off
            onNodeWithText(word.second).assertDoesNotExist()

            onNodeWithTag(TestTagAnswerTextField).performTextInput(word.second)
            onNodeWithTag(TestTagNextFlashcardButton).performClick()

            onNodeWithTag(TestTagTestHistory).assertExists()

            Thread.sleep(1000)
        }
    }

    private fun addCollectionWithName(name: String = "New collection") {
        with(composeTestRule) {
            onNodeWithTag(TestTagAddCollectionButton).performClick()
            waitForIdle()

            val nameTextField = onNodeWithTag(TestTagCollectionNameTextField)
            val sourceLangTextField = onNodeWithTag(TestTagCollectionSourceLanguageTextField)
            val sourceLangDropDown = onNodeWithTag(TestTagCollectionSourceLanguageDropdown)
            val targetLangTextField = onNodeWithTag(TestTagCollectionTargetLanguageTextField)
            val targetLangDropDown = onNodeWithTag(TestTagCollectionTargetLanguageDropdown)
            val saveButton = onNodeWithTag(TestTagCollectionSaveButton)

            nameTextField
                .performClick()
                .performTextInput(name)

            sourceLangTextField.performClick()
            sourceLangDropDown
                .onChildAt(0)
                .performClick()

            targetLangTextField.performClick()
            targetLangDropDown
                .onChildAt(1)
                .performClick()

            saveButton.performClick()
        }
    }

    private fun addWord(name: String = "Word", translation: String = "Translation") {
        with(composeTestRule) {
            val wordNameTextField = onNodeWithTag(TestTagWordNameTextField)
            val wordTranslationTextField = onNodeWithTag(TestTagWordTranslationTextField)
            val saveWordButton = onNodeWithTag(TestTagSaveWordButton)

            wordNameTextField
                .assertIsDisplayed()
                .performTextInput(name)
            wordTranslationTextField
                .assertIsDisplayed()
                .performTextInput(translation)
            saveWordButton
                .assertIsDisplayed()
                .performClick()
        }
    }
}
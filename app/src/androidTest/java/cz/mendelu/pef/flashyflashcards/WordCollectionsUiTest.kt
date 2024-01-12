package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
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
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionNameTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSaveButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionsLazyColumn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WordCollectionsUiTest {

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
    fun test_if_word_collections_empty() {
        with(composeTestRule) {
            onNodeWithTag(TestTagCollectionsLazyColumn).assertDoesNotExist()
        }
    }

    @Test
    fun add_word_collection() {
        with(composeTestRule) {
            onNodeWithTag(TestTagAddCollectionButton).performClick()

            waitForIdle()

            val nameTextField = onNodeWithTag(TestTagCollectionNameTextField)
            val sourceLangTextField = onNodeWithTag(TestTagCollectionSourceLanguageTextField)
            val sourceLangDropDown = onNodeWithTag(TestTagCollectionSourceLanguageDropdown)
            val targetLangTextField = onNodeWithTag(TestTagCollectionTargetLanguageTextField)
            val targetLangDropDown = onNodeWithTag(TestTagCollectionTargetLanguageDropdown)
            val saveButton = onNodeWithTag(TestTagCollectionSaveButton)

            val collectionName = "New collection"

            nameTextField
                .performClick()
                .performTextInput(collectionName)

            sourceLangTextField.performClick()
            val sourceLang = sourceLangDropDown.onChildAt(0)
            sourceLang.performClick()

            targetLangTextField.performClick()
            val targetLang = targetLangDropDown.onChildAt(1)
            targetLang.performClick()

            saveButton.performClick()

            waitForIdle()

            onNodeWithTag(TestTagCollectionsLazyColumn).assertExists()
            onNodeWithText(collectionName).assertExists()

            Thread.sleep(2000)
        }
    }
}
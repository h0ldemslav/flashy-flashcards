package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.flashyflashcards.navigation.DestinationsNavHostWrapper
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagAddCollectionButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionDeleteButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionNameTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSaveButton
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionSourceLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageDropdown
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionTargetLanguageTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTagCollectionsLazyColumn
import cz.mendelu.pef.flashyflashcards.ui.screens.collections.TestTageCollectionEditButton
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
    fun testAddWordCollection() {
        val name = "Animals"
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            onNodeWithText(name).assertExists()

            Thread.sleep(1000)
        }
    }

    @Test
    fun testEditWordCollection() {
        val name = "Jobs"
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            onNodeWithTag(TestTagCollectionsLazyColumn)
                .onChildAt(0)
                .performClick()
            waitForIdle()

            onNodeWithTag(TestTageCollectionEditButton).performClick()
            waitForIdle()

            val newName = "Sports"
            val nameTestField = onNodeWithTag(TestTagCollectionNameTextField)
            nameTestField.performTextClearance()
            nameTestField.performTextInput(newName)

            onNodeWithTag(TestTagCollectionSaveButton).performClick()
            waitForIdle()

            onNodeWithText(newName).assertExists()

            Thread.sleep(1000)
        }
    }

    @Test
    fun testDeleteWordCollection() {
        val name = "Travelling"
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            onNodeWithTag(TestTagCollectionsLazyColumn)
                .onChildAt(0)
                .performClick()
            waitForIdle()

            onNodeWithTag(TestTageCollectionEditButton).performClick()
            waitForIdle()

            onNodeWithTag(TestTagCollectionDeleteButton).performClick()
            waitForIdle()

            val removeDialogButton = composeTestRule.activity.getString(R.string.dialog_remove_label)
            onNodeWithText(removeDialogButton).performClick()
            waitForIdle()

            onNodeWithText(name).assertDoesNotExist()

            Thread.sleep(1000)
        }
    }

    @Test
    fun testCreationOfCollectionWithEmptyName() {
        val name = ""
        addCollectionWithName(name)

        with(composeTestRule) {
            waitForIdle()

            val errorMessage = composeTestRule.activity.getString(R.string.word_collections_collection_error)
            onNodeWithText(errorMessage).assertExists()

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
}
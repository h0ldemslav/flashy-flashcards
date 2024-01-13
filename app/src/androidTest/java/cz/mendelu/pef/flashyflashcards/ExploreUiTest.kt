package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.fake.FakeYelpAPIRepositoryImpl
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.ExploreScreen
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.ExploreScreenViewModel
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagNameTextField
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagSearchButton
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagSearchResultsList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ExploreUiTest {
    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    private lateinit var yelpAPIRepository: YelpAPIRepository
    private lateinit var navController: NavHostController
    private lateinit var viewModel: ExploreScreenViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        yelpAPIRepository = FakeYelpAPIRepositoryImpl()

        viewModel = ExploreScreenViewModel(
            yelpAPIRepository = yelpAPIRepository,
            dataStoreRepository = dataStoreRepository
        )

        composeTestRule.activity.setContent {
            navController = rememberNavController()

            ExploreScreen(
                navController = navController,
                viewModel = viewModel
            )

        }
    }

    @Test
    fun testDataFetch() {
        with(composeTestRule) {
            onNodeWithTag(TestTagNameTextField).performTextInput("Brno")
            onNodeWithTag(TestTagSearchButton).performClick()
            waitForIdle()

            onNodeWithTag(TestTagSearchResultsList)
                .assertExists()
                .assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testFetchDataFetchWithInvalidInput() {
        with(composeTestRule) {
            onNodeWithTag(TestTagNameTextField).performTextInput("")
            onNodeWithTag(TestTagSearchButton).performClick()
            onNodeWithText(composeTestRule.activity.getString(R.string.explore_screen_city_input_error)).assertIsDisplayed()

            Thread.sleep(1000)
        }
    }
}
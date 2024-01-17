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
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationError
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.fake.FakeYelpAPIRepositoryImpl
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.elements.TestTagPlaceholder
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
    private lateinit var fakeYelpAPIRepositoryImpl: FakeYelpAPIRepositoryImpl
    private lateinit var navController: NavHostController
    private lateinit var viewModel: ExploreScreenViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        fakeYelpAPIRepositoryImpl = FakeYelpAPIRepositoryImpl()

        viewModel = ExploreScreenViewModel(
            yelpAPIRepository = fakeYelpAPIRepositoryImpl,
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
            performPlacesSearch("Brno")

            onNodeWithTag(TestTagSearchResultsList)
                .assertExists()
                .assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testFetchDataFetchWithInvalidInput() {
        with(composeTestRule) {
            performPlacesSearch("")

            onNodeWithText(composeTestRule.activity.getString(R.string.explore_screen_city_input_error)).assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testNoConnectionPlaceholder() {
        with(composeTestRule) {
            fakeYelpAPIRepositoryImpl.setFakeResult(CommunicationResult.ConnectionError)

            performPlacesSearch("Bratislava")

            onNodeWithTag(TestTagPlaceholder)
                .assertExists()
                .assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testNoFoundPlaceholder() {
        with(composeTestRule) {
            val notFoundMessage = composeTestRule.activity.getString(R.string.city_not_found)

            fakeYelpAPIRepositoryImpl.setFakeResult(CommunicationResult.Error(
                CommunicationError(
                    code = 404,
                    message = notFoundMessage
                )
            ))

            performPlacesSearch("Praha")

            onNodeWithTag(TestTagPlaceholder)
                .assertExists()
                .assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    @Test
    fun testExceptionPlaceholder() {
        with(composeTestRule) {
            val unknownError = composeTestRule.activity.getString(R.string.unknown_error)

            fakeYelpAPIRepositoryImpl.setFakeResult(CommunicationResult.Exception(
                Throwable(unknownError))
            )

            performPlacesSearch("Stockholm")

            onNodeWithTag(TestTagPlaceholder)
                .assertExists()
                .assertIsDisplayed()

            Thread.sleep(2000)
        }
    }

    private fun performPlacesSearch(cityName: String) {
        with(composeTestRule) {
            onNodeWithTag(TestTagNameTextField).performTextInput(cityName)
            onNodeWithTag(TestTagSearchButton).performClick()
            waitForIdle()
        }
    }
}
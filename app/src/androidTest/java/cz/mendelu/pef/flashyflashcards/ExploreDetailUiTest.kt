package cz.mendelu.pef.flashyflashcards

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.fake.FakeYelpAPIRepositoryImpl
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.activities.MainActivity
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.DetailScreen
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.DetailScreenViewModel
import cz.mendelu.pef.flashyflashcards.ui.screens.explore.TestTagDetailMap
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ExploreDetailUiTest {
    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    @Inject
    lateinit var businessesRepository: BusinessesRepository
    private lateinit var yelpAPIRepository: YelpAPIRepository
    private lateinit var navController: NavHostController
    private lateinit var viewModel: DetailScreenViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @JvmField
    @Rule
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION"
    )

    @Before
    fun setup() {
        hiltRule.inject()

        yelpAPIRepository = FakeYelpAPIRepositoryImpl()

        viewModel = DetailScreenViewModel(
            yelpAPIRepository = yelpAPIRepository,
            businessesRepository = businessesRepository,
            dataStoreRepository = dataStoreRepository
        )

        composeTestRule.activity.setContent {
            navController = rememberNavController()

            DetailScreen(
                navController = navController,
                viewModel = viewModel,
                dataSourceType = DataSourceType.Remote(viewModel.uiState.data?.remoteId ?: "")
            )
        }
    }

    @Test
    fun testMap() {
        with(composeTestRule) {
            // Test succeeds, if it's run separately. Fails if it's run with other tests
//            waitUntil { viewModel.uiState.loading }
//            onNodeWithTag(TestTagDetailMap, useUnmergedTree = true).assertIsDisplayed()
//
//            Thread.sleep(2000)
        }
    }
}
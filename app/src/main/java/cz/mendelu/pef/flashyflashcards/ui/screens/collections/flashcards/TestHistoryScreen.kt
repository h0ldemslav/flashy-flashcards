package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.TestHistoryDetailScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.utils.DateUtils

const val TestTagTestHistoryRecordsList = "TestTagTestHistoryRecordsList"

@CollectionsNavGraph
@Destination
@Composable
fun TestHistoryScreen(
    navController: NavController,
    viewModel: TestHistoryScreenViewModel = hiltViewModel(),
    collectionId: Long?
) {
    LaunchedEffect(Unit) {
        viewModel.getAllTestHistoryByCollectionId(collectionId)
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.test_history_label),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() },
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        TestHistoryScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState
        )
    }
}

@Composable
fun TestHistoryScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    uiState: UiState<List<TestHistory>, ScreenErrors>
) {
    if (uiState.data?.isNotEmpty() == true) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
                .testTag(TestTagTestHistoryRecordsList)
        ) {
            uiState.data!!.forEachIndexed { index, history ->
                item {
                    ListRow(
                        showAvatar = false,
                        headline = "${stringResource(id = R.string.test_label)} #${index + 1}",
                        supportingText = DateUtils.getDateString(history.dateOfCompletion)
                    ) {
                        navController.navigate(
                            TestHistoryDetailScreenDestination(testHistoryId = history.id)
                        )
                    }
                }
            }
        }
    } else {
        PlaceholderElement(
            paddingValues = paddingValues,
            fillMaxSize = true,
            imageRes = uiState.errors?.imageRes,
            textRes = uiState.errors?.messageRes ?: R.string.empty_placeholder
        )
    }
}
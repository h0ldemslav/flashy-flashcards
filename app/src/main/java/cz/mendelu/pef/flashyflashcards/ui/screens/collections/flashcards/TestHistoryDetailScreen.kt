package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicAlertDialog
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

const val TestTagTestHistoryDetailDeleteButton = "TestTagTestHistoryDetailDeleteButton"

@CollectionsNavGraph
@Destination
@Composable
fun TestHistoryDetailScreen(
    navController: NavController,
    viewModel: TestHistoryDetailScreenViewModel = hiltViewModel(),
    testHistoryId: Long?
) {
    var isRemoveTestHistoryDialogOpened by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.getTestHistoryById(testHistoryId = testHistoryId)
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.test_history_detail_label),
        onBackClick = { navController.popBackStack() },
        showLoading = viewModel.uiState.loading,
        actions = {
            if (viewModel.uiState.data != null) {
                IconButton(
                    onClick = {
                        isRemoveTestHistoryDialogOpened = true
                    },
                    modifier = Modifier.testTag(TestTagTestHistoryDetailDeleteButton)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_label)
                    )
                }
            }
        }
    ) { paddingValues ->
        TestHistoryDetailScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState
        )

        if (isRemoveTestHistoryDialogOpened) {
            BasicAlertDialog(
                title = stringResource(id = R.string.test_history_dialog_title),
                body = stringResource(id = R.string.test_history_dialog_content),
                onDismissRequest = {
                    isRemoveTestHistoryDialogOpened = false
                }
            ) {
                    isRemoveTestHistoryDialogOpened = false

                    viewModel.deleteTestHistory()
                    navController.popBackStack()
            }
        }
    }
}

@Composable
fun TestHistoryDetailScreenContent(
    uiState: UiState<TestHistory, ScreenErrors>,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
    ) {
        if (uiState.data != null) {
            TestSummary(
                testHistory = uiState.data!!,
                modifier = Modifier.testTag(TestTagTestSummary)
            )
        } else if (uiState.errors != null) {
            PlaceholderElement(
                imageRes = uiState.errors!!.imageRes,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}
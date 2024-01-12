package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.AddEditWordCollectionScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.WordsScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

const val TestTagCollectionsLazyColumn = "TestTagCollectionsLazyColumn"
const val TestTagAddCollectionButton = "TestTagAddCollectionButton"

@CollectionsNavGraph(start = true)
@Destination
@Composable
fun WordCollectionsScreen(
    navController: NavController,
    viewModel: WordCollectionsViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.collections),
        bottomAppBar = {
            BottomBar(navController = navController)
        },
        showLoading = viewModel.uiState.loading,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        AddEditWordCollectionScreenDestination(wordCollectionId = null)
                    )
                },
                modifier = Modifier.testTag(TestTagAddCollectionButton)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) { paddingValues ->
        WordCollectionsScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            onRowClick = {
                navController.navigate(
                    WordsScreenDestination(
                        collectionId = it.id,
                        collectionName = it.name
                    )
                )
            }
        )
    }
}

@Composable
fun WordCollectionsScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<MutableList<WordCollection>, ScreenErrors>,
    onRowClick: (WordCollection) -> Unit
) {
    if (uiState.data?.isNotEmpty() == true) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
                .testTag(TestTagCollectionsLazyColumn)
        ) {
            uiState.data!!.forEach { col ->
                item {
                    ListRow(
                        headline = col.name,
                        supportingText = "${col.sourceLanguage} - ${col.targetLanguage}",
                        onRowClick = {
                            onRowClick(col)
                        }
                    )
                }
            }
        }
    } else {
        PlaceholderElement(
            imageRes = uiState.errors?.imageRes,
            textRes = uiState.errors?.messageRes ?: R.string.empty_placeholder,
            paddingValues = paddingValues,
            fillMaxSize = true
        )
    }
}
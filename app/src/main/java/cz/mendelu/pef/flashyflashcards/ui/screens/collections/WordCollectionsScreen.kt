package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            FloatingActionButton(onClick = {
                navController.navigate(AddEditWordCollectionScreenDestination(wordCollection = null))
            }) {
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
            onIconClick = {
                navController.navigate(AddEditWordCollectionScreenDestination(wordCollection = it))
            },
            onRowClick = {
                navController.navigate(WordsScreenDestination(wordCollection = it))
            }
        )
    }
}

@Composable
fun WordCollectionsScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<MutableList<WordCollection>, ScreenErrors>,
    onIconClick: (WordCollection) -> Unit,
    onRowClick: (WordCollection) -> Unit
) {
    if (uiState.data?.isNotEmpty() == true) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            uiState.data!!.forEach { col ->
                item {
                    ListRow(
                        headline = col.name,
                        supportingText = "${col.sourceLanguage} - ${col.targetLanguage}",
                        actionIcon = Icons.Default.Edit,
                        actionIconDescription = stringResource(id = R.string.edit_label),
                        onActionIconClick = {
                            onIconClick(col)
                        },
                        onRowClick = {
                            onRowClick(col)
                        }
                    )
                }
            }
        }
    } else if (uiState.errors != null) {
        PlaceholderElement(
            imageRes = uiState.errors!!.imageRes,
            textRes = uiState.errors!!.messageRes
        )
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = stringResource(id = R.string.empty_collections_placeholder))
        }
    }
}
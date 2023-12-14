package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.AddEditWordCollectionScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.AddEditWordScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@CollectionsNavGraph
@Destination
@Composable
fun WordsScreen(
    navController: NavController,
    viewModel: WordsScreenViewModel = hiltViewModel(),
    collectionId: Long?,
    collectionName: String
) {
    LaunchedEffect(Unit) {
        viewModel.getAllWordCollectionWords(collectionId)
    }

    val savedStateHandleKey = "updated_collection_name"
    val updatedCollectionName = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>(savedStateHandleKey)

    BasicScaffold(
        topAppBarTitle = updatedCollectionName ?: collectionName,
        showLoading = viewModel.uiState.loading,
        bottomAppBar = {
           BottomBar(navController = navController)
        },
        onBackClick = {
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(savedStateHandleKey)
            navController.popBackStack()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(
                    AddEditWordScreenDestination(
                        collectionId = collectionId,
                        word = null
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(
                    AddEditWordCollectionScreenDestination(wordCollectionId = collectionId)
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_label)
                )
            }
        }
    ) { paddingValues ->
        WordsScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState
        )
    }
}

@Composable
fun WordsScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    uiState: UiState<List<Word>, ScreenErrors>
) {
    if (uiState.data?.isNotEmpty() == true) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            uiState.data!!.forEach { word ->
                item {
                    ListRow(
                        showAvatar = false,
                        headline = word.name,
                        supportingText = word.translation,
                        onRowClick = {
                            navController.navigate(
                                AddEditWordScreenDestination(
                                    collectionId = word.wordCollectionId,
                                    word = word
                                )
                            )
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
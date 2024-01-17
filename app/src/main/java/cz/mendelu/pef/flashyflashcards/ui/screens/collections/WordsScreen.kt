package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.FlashcardPracticeType
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
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.FlashcardPracticeScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.TestHistoryScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

const val TestTagCollectionEditButton = "TestTagCollectionEditButton"
const val TestTagAddWordButton = "TestTagAddWordButton"
const val TestTagTrainingMode = "TestTagTrainingMode"
const val TestTagTestMode = "TestTagTestMode"
const val TestTagCollectionsMoreActions = "TestTagCollectionsMoreActions"
const val TestTagTestHistory = "TestTagTestHistory"

@CollectionsNavGraph
@Destination
@Composable
fun WordsScreen(
    navController: NavController,
    viewModel: WordsScreenViewModel = hiltViewModel(),
    collectionId: Long?,
    collectionName: String
) {
    val testAnswerLength by viewModel.getTestAnswerLength()
        .collectAsStateWithLifecycle(initialValue = 0L)

    LaunchedEffect(Unit) {
        if (viewModel.uiState.data == null) {
            viewModel.getAllWordCollectionWords(collectionId)
        }
    }

    val savedStateHandleKey = "updated_collection_name"
    val updatedCollectionName = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>(savedStateHandleKey)

    var popupMenuExpanded by remember { mutableStateOf(false) }

    BasicScaffold(
        topAppBarTitle = updatedCollectionName ?: collectionName,
        showLoading = viewModel.uiState.loading,
        bottomAppBar = {
           BottomBar(navController = navController)
        },
        onBackClick = {
            viewModel.closeAndResetTranslator()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(savedStateHandleKey)
            navController.popBackStack()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        AddEditWordScreenDestination(
                            collectionId = collectionId,
                            word = null
                        )
                    )
                },
                modifier = Modifier.testTag(TestTagAddWordButton)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(
                        AddEditWordCollectionScreenDestination(wordCollectionId = collectionId)
                    )
                },
                modifier = Modifier.testTag(TestTagCollectionEditButton)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_label)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(
                            FlashcardPracticeScreenDestination(
                                collectionId = collectionId,
                                flashcardPracticeType = FlashcardPracticeType.Training
                            )
                        )
                },
                modifier = Modifier.testTag(TestTagTrainingMode)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = stringResource(id = R.string.training_label)
                )
            }

            IconButton(
                onClick = {
                    popupMenuExpanded = true
                },
                modifier = Modifier.testTag(TestTagCollectionsMoreActions)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.more_label)
                )
            }
        }
    ) { paddingValues ->
        WordsScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState
        )

        if (popupMenuExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = basicMargin())
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                DropdownMenu(
                    expanded = popupMenuExpanded,
                    onDismissRequest = {
                        popupMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.test_label))
                        },
                        onClick = {
                            popupMenuExpanded = false

                            navController.navigate(
                                FlashcardPracticeScreenDestination(
                                    collectionId = collectionId,
                                    flashcardPracticeType = FlashcardPracticeType.Test(
                                        testAnswerLength
                                    )
                                )
                            )
                        },
                        modifier = Modifier.testTag(TestTagTestMode)
                    )

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.test_history_label))
                        },
                        onClick = {
                            navController.navigate(
                                TestHistoryScreenDestination(collectionId = collectionId)
                            )
                        },
                        modifier = Modifier.testTag(TestTagTestHistory)
                    )
                }
            }
        }
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
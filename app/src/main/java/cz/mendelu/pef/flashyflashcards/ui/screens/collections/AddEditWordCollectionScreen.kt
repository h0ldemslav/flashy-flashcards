package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicAlertDialog
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.DropDownElement
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.WordCollectionsScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.WordsScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@CollectionsNavGraph
@Destination
@Composable
fun AddEditWordCollectionScreen(
    navController: NavController,
    viewModel: AddEditWordCollectionScreenViewModel = hiltViewModel(),
    wordCollectionId: Long?,
) {
    var isRemoveCollectionDialogOpened by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (wordCollectionId != null) {
            viewModel.getWordCollectionById(wordCollectionId)
        }
    }

    BasicScaffold(
        topAppBarTitle = if (wordCollectionId != null)
            stringResource(id = R.string.edit_collection)
        else
            stringResource(id = R.string.new_collection),
        bottomAppBar = {
           BottomBar(navController = navController)
        },
        onBackClick = { navController.popBackStack() },
        actions = {
            if (wordCollectionId != null) {
                IconButton(onClick = {
                    isRemoveCollectionDialogOpened = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_label)
                    )
                }
            }
        }
    ) { paddingValues ->
        AddEditWordCollectionScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState,
            actions = viewModel
        )

        if (isRemoveCollectionDialogOpened) {
            BasicAlertDialog(
                title = stringResource(id = R.string.collection_dialog_title),
                body = stringResource(id = R.string.collection_dialog_content),
                onDismissRequest = {
                    isRemoveCollectionDialogOpened = false
            }) {
                isRemoveCollectionDialogOpened = false

                if (viewModel.uiState.data != null) {
                    viewModel.deleteWordCollection(viewModel.uiState.data!!)
                    navController.navigate(WordCollectionsScreenDestination) {
                        popUpTo(WordsScreenDestination) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditWordCollectionScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    actions: AddEditWordCollectionScreenActions,
    uiState: UiState<WordCollection, ScreenErrors>
) {
    var isSourceLanguageExpanded by remember {
        mutableStateOf(false)
    }
    var isTargetLanguageExpanded by remember {
        mutableStateOf(false)
    }

    val languages = actions.getAllLanguages()
    val languagesFullNames = languages.keys.toList()

    if (uiState.data != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(basicMargin()),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(basicMargin())
        ) {
            BasicTextFieldElement(
                value = uiState.data!!.name,
                onValueChange = {
                    uiState.data!!.name = it
                    actions.setWordCollection(uiState.data!!)
                },
                label = stringResource(id = R.string.name_label),
                errorMessage = if (uiState.errors?.messageRes == R.string.word_collections_collection_error)
                        stringResource(id = uiState.errors!!.messageRes)
                else
                    null
            )

            DropDownElement(
                items = languagesFullNames,
                selectedItem = uiState.data!!.sourceLanguage,
                label = stringResource(id = R.string.source_language_label),
                isExpanded = isSourceLanguageExpanded,
                onExpandedChange = { isSourceLanguageExpanded = it },
                onDismissRequest = { isSourceLanguageExpanded = false },
                onDropDownMenuItemClick = { _, item ->
                    uiState.data!!.sourceLanguage = item
                    actions.setWordCollection(uiState.data!!)

                    isSourceLanguageExpanded = false
                }
            )

            DropDownElement(
                items = languagesFullNames,
                selectedItem = uiState.data!!.targetLanguage,
                label = stringResource(id = R.string.target_language_label),
                errorMessage = if (uiState.errors?.messageRes == R.string.word_collections_languages_error)
                    stringResource(id = uiState.errors!!.messageRes)
                else
                    null
                ,
                isExpanded = isTargetLanguageExpanded,
                onExpandedChange = { isTargetLanguageExpanded = it },
                onDismissRequest = { isTargetLanguageExpanded = false },
                onDropDownMenuItemClick = { _, item ->
                    uiState.data!!.targetLanguage = item
                    actions.setWordCollection(uiState.data!!)

                    isTargetLanguageExpanded = false
                }
            )

            Button(
                onClick = {
                    val isWordCollectionValid = actions.isWordCollectionValid(uiState.data!!)

                    if (isWordCollectionValid) {
                        actions.saveWordCollection(uiState.data!!)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("updated_collection_name", uiState.data!!.name)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.padding(top = basicMargin())
            ) {
                Text(text = stringResource(id = R.string.save_label))
            }
        }
    } else if (uiState.errors != null) {
        PlaceholderElement(
            imageRes = uiState.errors!!.imageRes,
            textRes = uiState.errors!!.messageRes,
            paddingValues = paddingValues,
            fillMaxSize = true
        )
    }
}
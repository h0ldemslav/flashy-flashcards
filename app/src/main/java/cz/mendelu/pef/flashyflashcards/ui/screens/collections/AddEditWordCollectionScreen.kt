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
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.DropDownElement
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin

@CollectionsNavGraph
@Destination
@Composable
fun AddEditWordCollectionScreen(
    wordCollection: WordCollection?,
    navController: NavController,
    viewModel: AddEditWordCollectionScreenViewModel = hiltViewModel()
) {
    wordCollection?.let {
        viewModel.screenData.wordCollection = it
    }

    BasicScaffold(
        topAppBarTitle = if (wordCollection != null)
            stringResource(id = R.string.edit_collection)
        else
            stringResource(id = R.string.new_collection),
        bottomAppBar = {
           BottomBar(navController = navController)
        },
        onBackClick = { navController.popBackStack() },
        actions = {
            if (wordCollection != null) {
                IconButton(onClick = { /*TODO*/ }) {
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
            screenData = viewModel.screenData,
            actions = viewModel,
            onScreenDataChange = {
                viewModel.screenData = it
            },
            onSaveButtonClick = {
                viewModel.validateScreenData()

                if (viewModel.screenData.wordCollectionError == null) {
                    viewModel.saveWordCollection()
                    navController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun AddEditWordCollectionScreenContent(
    paddingValues: PaddingValues,
    actions: AddEditWordCollectionScreenActions,
    screenData: AddEditWordCollectionScreenData,
    onScreenDataChange: (AddEditWordCollectionScreenData) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    var isSourceLanguageExpanded by remember {
        mutableStateOf(false)
    }
    var isTargetLanguageExpanded by remember {
        mutableStateOf(false)
    }

    val languages = actions.getAllLanguages()
    val languagesFullNames = languages.keys.toList()

    Column(
        verticalArrangement = Arrangement.spacedBy(basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(basicMargin())
    ) {
        BasicTextFieldElement(
            value = screenData.wordCollection.name,
            onValueChange = {
                val col = screenData.wordCollection.copy(name = it)
                onScreenDataChange(screenData.copy(wordCollection = col))
            },
            label = stringResource(id = R.string.name_label),
            errorMessage = if (screenData.wordCollectionError == R.string.word_collections_collection_error)
                stringResource(id = screenData.wordCollectionError!!)
            else
                null
        )

        DropDownElement(
            items = languagesFullNames,
            selectedItem = screenData.wordCollection.sourceLanguage,
            label = stringResource(id = R.string.source_language_label),
            isExpanded = isSourceLanguageExpanded,
            onExpandedChange = { isSourceLanguageExpanded = it },
            onDismissRequest = { isSourceLanguageExpanded = false },
            onDropDownMenuItemClick = {
                val col = screenData.wordCollection.copy(sourceLanguage = it)
                onScreenDataChange(screenData.copy(wordCollection = col))

                isSourceLanguageExpanded = false
            }
        )

        DropDownElement(
            items = languagesFullNames,
            selectedItem = screenData.wordCollection.targetLanguage,
            label = stringResource(id = R.string.target_language_label),
            errorMessage = if (screenData.wordCollectionError == R.string.word_collections_languages_error)
                stringResource(id = screenData.wordCollectionError!!)
            else
                null,
            isExpanded = isTargetLanguageExpanded,
            onExpandedChange = { isTargetLanguageExpanded = it },
            onDismissRequest = { isTargetLanguageExpanded = false },
            onDropDownMenuItemClick = {
                val col = screenData.wordCollection.copy(targetLanguage = it)
                onScreenDataChange(screenData.copy(wordCollection = col))

                isTargetLanguageExpanded = false
            }
        )
        
        Button(
            onClick = {
                onSaveButtonClick()
            },
            modifier = Modifier.padding(top = basicMargin())
        ) {
            Text(text = stringResource(id = R.string.save_label))
        }
    }
}
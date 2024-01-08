package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.LoadingScreenCircleIndicator
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin

@CollectionsNavGraph
@Destination
@Composable
fun AddEditWordScreen(
    navController: NavController,
    viewModel: AddEditWordScreenViewModel = hiltViewModel(),
    collectionId: Long?,
    word: Word?
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setWord(word, collectionId)
    }

    BasicScaffold(
        topAppBarTitle = if (word != null)
            stringResource(id = R.string.edit_word)
        else
            stringResource(id = R.string.new_word)
        ,
        bottomAppBar = {
            BottomBar(navController = navController)
        },
        onBackClick = {
            navController.popBackStack()
        },
        actions = {
            if (viewModel.uiState.data?.id != null) {
                IconButton(
                    enabled = !viewModel.uiState.loading,
                    onClick = {
                        viewModel.deleteWord(viewModel.uiState.data!!)
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_label)
                    )
                }
            }
        }
    ) { paddingValues ->
        AddEditWordScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            context = context,
            uiState = viewModel.uiState,
            actions = viewModel
        )
    }
}

@Composable
fun AddEditWordScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    context: Context,
    uiState: UiState<Word, ScreenErrors>,
    actions: AddEditWordScreenActions
) {
    if (uiState.data != null) {
        // MLKit failed
        if (uiState.errors != null) {
            Toast.makeText(
                context,
                context.getString(uiState.errors!!.messageRes),
                Toast.LENGTH_SHORT
            ).show()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(basicMargin()),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            BasicTextFieldElement(
                value = uiState.data!!.name,
                onValueChange = {
                    uiState.data!!.name = it
                    actions.setWord(uiState.data!!)
                },
                label = stringResource(id = R.string.name_label),
                errorMessage = if (uiState.errors?.messageRes == R.string.words_name_error)
                    stringResource(id = uiState.errors!!.messageRes)
                else
                    null
            )

            BasicTextFieldElement(
                value = uiState.data!!.translation,
                onValueChange = {
                    uiState.data!!.translation = it
                    actions.setWord(uiState.data!!)
                },
                label = stringResource(id = R.string.translation_label),
                errorMessage = if (uiState.errors?.messageRes == R.string.words_translation_error)
                    stringResource(id = uiState.errors!!.messageRes)
                else
                    null
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(basicMargin()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = basicMargin())
                    .padding(horizontal = basicMargin())
            ) {
                Button(
                    enabled = !uiState.loading,
                    onClick = {
                        actions.translateWord(uiState.data!!)
                    },
                    modifier = Modifier.weight(0.4f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = stringResource(id = R.string.translate_label))
                }

                Button(
                    enabled = !uiState.loading,
                    onClick = {
                        val isWordValid = actions.isWordValid(uiState.data!!)

                        if (isWordValid) {
                            actions.saveWord(uiState.data!!)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(0.4f)
                ) {
                    Text(text = stringResource(id = R.string.save_label))
                }
            }

            Text(
                text = stringResource(id = R.string.translate_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = halfMargin())
            )

            if (uiState.loading) {
                LoadingScreenCircleIndicator(
                    modifier = Modifier.padding(vertical = basicMargin())
                )
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
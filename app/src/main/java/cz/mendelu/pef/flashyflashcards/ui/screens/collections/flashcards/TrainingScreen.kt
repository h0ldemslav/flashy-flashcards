package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

@CollectionsNavGraph
@Destination
@Composable
fun TrainingScreen(
    navController: NavController,
    viewModel: TrainingScreenViewModel = hiltViewModel(),
    collectionId: Long?
) {
    LaunchedEffect(Unit) {
        if (viewModel.uiState.data == null) {
            viewModel.getAllCollectionWords(collectionId)
        }
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.training_label),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        TrainingScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            actions = viewModel
        )
    }
}

@Composable
fun TrainingScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Word, ScreenErrors>,
    actions: TrainingScreenActions
) {
    val boxHeight = 156.dp
    var boxText by remember {
        mutableStateOf("")
    }
    var answer by remember {
        mutableStateOf("")
    }

    if (uiState.data != null) {
        if (boxText.isEmpty()) {
            boxText = uiState.data!!.name
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(mediumMargin()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(basicMargin())
                .padding(top = basicMargin())
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxHeight)
                    .clip(RoundedCornerShape(basicMargin()))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        boxText = if (boxText == uiState.data!!.name) {
                            uiState.data!!.translation
                        } else {
                            uiState.data!!.name
                        }
                    }
            ) {
                Text(
                    text = boxText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(smallMargin()))

            BasicTextFieldElement(
                value = answer,
                onValueChange = {
                    answer = it
                },
                label = stringResource(id = R.string.answer_label),
                supportingText = stringResource(id = R.string.flashcard_hint),
                errorMessage =  if (uiState.errors != null)
                    stringResource(id = uiState.errors!!.messageRes)
                else
                    null
            )

            ElevatedButton(onClick = {
                if (actions.isAnswerCorrect(answer)) {
                    actions.setNextWord()
                    answer = ""
                    boxText = ""
                }
            }) {
                Text(text = stringResource(id = R.string.next_label))
            }
        }
    }
}